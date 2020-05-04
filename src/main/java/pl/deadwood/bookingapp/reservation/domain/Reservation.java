package pl.deadwood.bookingapp.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.deadwood.bookingapp.screening.domain.SeatId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Reservation {

    static final int MINIMUM_SEATS_COUNT = 1;

    @NonNull
    private final UUID id;

    @NonNull
    private final UUID screeningId;

    @NonNull
    private final Set<ReservationSeat> reservationSeats;

    @NonNull
    private final Name name;

    @NonNull
    private final Surname surname;

    @NonNull
    private final Email email;

    @NonNull
    private final Instant expireTime;

    @NonNull
    private Status status;

    void confirm() {
        status = Status.CONFIRMED;
    }

    void cancel() {
        status = Status.CANCELED;
    }

    boolean notConfirmed() {
        return Status.CONFIRMED != status;
    }

    boolean notCanceled() {
        return Status.CANCELED != status;

    }

    void cancelSeats(@NonNull Set<SeatId> seatId) {
        reservationSeats.removeIf(reservationSeat -> seatId.contains(reservationSeat.getId()));
    }

    Set<SeatId> getSeatIds() {
        return reservationSeats.stream()
                .map(ReservationSeat::getId)
                .collect(toSet());
    }

    BigDecimal getAmountToPay() {
        return reservationSeats
                .stream()
                .map(reservationSeat -> reservationSeat.getTicket().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Reservation of(NewReservation newReservation) {
        checkMinimumSeatsNumber(newReservation.getReservationSeats());
        UUID newId = UUID.randomUUID();
        return new Reservation(newId,
                newReservation.getScreeningId(),
                Set.copyOf(newReservation.getReservationSeats()),
                newReservation.getName(),
                newReservation.getSurname(),
                newReservation.getEmail(),
                newReservation.getExpireTime(),
                Status.NEW);
    }

    private static void checkMinimumSeatsNumber(Set<ReservationSeat> reservationSeats) {
        if (reservationSeats.size() < MINIMUM_SEATS_COUNT) {
            throw new ReservationException(format("You have to book at least [%d] seats, but got [%d] seats", MINIMUM_SEATS_COUNT, reservationSeats.size()));
        }
    }

}
