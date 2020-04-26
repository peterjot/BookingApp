package pl.deadwood.bookingapp.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.deadwood.bookingapp.reservation.domain.dto.CreatedReservation;
import pl.deadwood.bookingapp.reservation.domain.dto.ReservationCommand;
import pl.deadwood.bookingapp.screening.domain.Screenings;
import pl.deadwood.bookingapp.screening.domain.SeatId;
import pl.deadwood.bookingapp.screening.domain.dto.ScreeningInfo;
import pl.deadwood.bookingapp.screening.domain.dto.Seat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

@Slf4j
@AllArgsConstructor
public class Reservations {

    private static final int MINUTES_BEFORE_SCREENING_TO_CANCEL_RESERVATION = 15;
    private static final int MINUTES_BEFORE_SCREENING_TO_RESERVE = 15;
    private static final int MINUTES_TO_EXPIRE_RESERVATION = 15;

    @NonNull
    private final Clock clock;

    @NonNull
    private final Screenings screenings;

    @NonNull
    private final ReservationRepository reservationRepository;

    @NonNull
    private final EventPublisher eventPublisher;


    public CreatedReservation book(@NonNull ReservationCommand reservationCommand) {
        log.debug("Creating a new reservation... ReservationCommand=[{}]", reservationCommand);
        ScreeningInfo screeningInfo = getValidScreeningInfo(reservationCommand);

        screenings.reserveSeats(reservationCommand.getScreeningId(), reservationCommand.getSeatIds());

        CreatedReservation createdReservation = saveReservation(reservationCommand, screeningInfo.getStart());
        log.debug("Created new reservation=[{}]", createdReservation);
        return createdReservation;
    }

    public void cancelExpiredReservations() {
        reservationRepository
                .findByTimeBefore(now())
                .stream()
                .filter(Reservation::notCanceled)
                .forEach(reservation -> {
                    log.debug("Canceling reservation=[{}]", reservation);
                    reservation.cancel();
                    screenings.releaseSeats(reservation.getScreeningId(), reservation.getSeatIds());
                });

    }

    public void releaseSeats(@NonNull UUID screeningId, @NonNull Set<SeatId> seatIds) {
        log.debug("OnSeatsReleasedEvent - Releasing seats screeningId=[{}], seats=[{}]", screeningId, seatIds);
        Set<Reservation> reservations = reservationRepository.findByScreeningId(screeningId);

        reservations
                .stream()
                .filter(Reservation::notConfirmed)
                .forEach(reservation -> reservation.cancelSeats(seatIds));

        reservationRepository.update(reservations);
    }

    public void confirmReservation(@NonNull UUID reservationId) {
        log.debug("OnReservationConfirmedEvent - Confirming reservation reservationId=[{}]", reservationId);
        reservationRepository
                .findById(reservationId)
                .ifPresent(reservation -> {
                    reservation.confirm();
                    reservationRepository.update(reservation);
                });
    }

    private CreatedReservation saveReservation(ReservationCommand reservationCommand, Instant screeningStart) {

        Reservation reservation = reservationRepository.save(
                NewReservation.of(
                        reservationCommand,
                        timeToExpireReservation(screeningStart)));

        eventPublisher.publish(new OnReservationCreatedEvent(reservation.getId(), reservation.getEmail()));

        return new CreatedReservation(
                reservation.getId(),
                reservation.getAmountToPay(),
                reservation.getExpireTime());
    }

    private Instant timeToExpireReservation(Instant screeningStart) {
        Instant toCancelBeforeMovieStarts = timeToCancelBeforeMovieStarts(screeningStart);
        Instant timeToExpireReservation = timeToExpireReservation();

        if (toCancelBeforeMovieStarts.isBefore(timeToExpireReservation)) {
            return toCancelBeforeMovieStarts;
        }

        return timeToExpireReservation;
    }

    private Instant timeToCancelBeforeMovieStarts(Instant screeningStart) {
        return screeningStart.minus(MINUTES_BEFORE_SCREENING_TO_CANCEL_RESERVATION, ChronoUnit.MINUTES);
    }

    private Instant timeToExpireReservation() {
        return now().plus(MINUTES_TO_EXPIRE_RESERVATION, ChronoUnit.MINUTES);
    }


    private ScreeningInfo getValidScreeningInfo(ReservationCommand reservationCommand) {
        UUID screeningId = reservationCommand.getScreeningId();
        ScreeningInfo screeningInfo = screenings
                .find(screeningId)
                .orElseThrow(() -> new ReservationException(format("Cannot book screening which doesnt exist! screeningId=[%s]", screeningId)));

        checkReservationSeats(screeningInfo.getSeats(), reservationCommand.getReservationSeats());
        checkReservationTime(screeningInfo);
        return screeningInfo;
    }

    private void checkReservationSeats(Set<Seat> actualSeats, Set<ReservationSeat> reservationSeats) {
        Map<SeatId, Seat> actualSeatsMap = getSeatsMap(actualSeats);

        tryToReserveSeats(actualSeatsMap, reservationSeats);

        actualSeatsMap
                .values()
                .forEach(seat -> validateSeatNeighbor(actualSeatsMap, seat));
    }

    private void tryToReserveSeats(Map<SeatId, Seat> actualSeats, Set<ReservationSeat> reservationSeats) {
        reservationSeats
                .forEach(reservationSeat -> tryToReserveSeat(actualSeats, reservationSeat));
    }

    private void tryToReserveSeat(Map<SeatId, Seat> actualSeats, ReservationSeat reservationSeat) {
        checkThatSeatIsAvailable(actualSeats, reservationSeat);
        actualSeats.remove(reservationSeat.getId());
    }

    private void validateSeatNeighbor(Map<SeatId, Seat> actualSeatsMap, Seat seat) {
        Seat leftNeighbor = findLeftSibling(actualSeatsMap, seat);
        Seat rightNeighbor = findRightSibling(actualSeatsMap, seat);
        if (isFreeSeatBetweenToReserved(leftNeighbor, rightNeighbor)) {
            throw new ReservationException("Cannot book cuz cannot be a single place left over " +
                    "in a row between two already reserved places.");
        }
    }

    private boolean isFreeSeatBetweenToReserved(Seat leftNeighbor, Seat rightNeighbor) {
        return leftNeighbor != null && rightNeighbor != null &&
                !leftNeighbor.available() && !rightNeighbor.available();
    }

    private Seat findRightSibling(Map<SeatId, Seat> availableSeats, Seat ava) {
        return availableSeats.get(
                SeatId.of(ava.getId().getRow(),
                        ava.getId().getColumn() + 1));
    }

    private Seat findLeftSibling(Map<SeatId, Seat> availableSeats, Seat ava) {
        return availableSeats.get(
                SeatId.of(
                        ava.getId().getRow(),
                        ava.getId().getColumn() - 1));
    }

    private void checkThatSeatIsAvailable(Map<SeatId, Seat> actualSeats, ReservationSeat reservationSeat) {
        Seat seat = actualSeats.get(reservationSeat.getId());

        if (seat == null || !seat.available()) {
            throw new ReservationException(format("This seat is not available at id=[[%s]]. Try make reservation again",
                    reservationSeat.getId()));
        }
    }

    private Map<SeatId, Seat> getSeatsMap(Set<Seat> actualSeatsList) {
        return actualSeatsList
                .stream()
                .collect(toMap(Seat::getId, Function.identity()));
    }

    private void checkReservationTime(ScreeningInfo screeningInfo) {
        Instant now = now();
        Instant start = screeningInfo.getStart();

        long secondsToScreening = Duration.between(now, start).toSeconds();

        int minSecondsToMakeReservation = MINUTES_BEFORE_SCREENING_TO_RESERVE * 60;
        if (secondsToScreening < minSecondsToMakeReservation) {
            throw new ReservationException(format("Seats can be booked at latest [%d] minutes before the screening begins",
                    MINUTES_BEFORE_SCREENING_TO_RESERVE));
        }
    }

    private Instant now() {
        return Instant.now(clock);
    }
}
