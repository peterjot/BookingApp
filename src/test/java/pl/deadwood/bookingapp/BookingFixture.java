package pl.deadwood.bookingapp;

import pl.deadwood.bookingapp.reservation.domain.Email;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Surname;
import pl.deadwood.bookingapp.reservation.domain.Ticket;
import pl.deadwood.bookingapp.reservation.domain.dto.ReservationCommand;
import pl.deadwood.bookingapp.screening.domain.Room;
import pl.deadwood.bookingapp.screening.domain.SeatId;
import pl.deadwood.bookingapp.screening.domain.SeatStatus;
import pl.deadwood.bookingapp.screening.domain.dto.ScreeningInfo;
import pl.deadwood.bookingapp.screening.domain.dto.Seat;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class BookingFixture {

    private BookingFixture() {
        throw new UnsupportedOperationException();
    }

    public static Instant maxDuration() {
        return Instant.MAX;
    }

    public static ReservationCommand reservationCommand(UUID screeningUuid) {
        return new ReservationCommand(screeningUuid, seatsToReserve(), "Piotrek", "Jasina", "piotr@gmail.com");
    }

    public static Set<ReservationSeat> seatsToReserve() {
        return Set.of(
                new ReservationSeat(SeatId.of(1, 1), Ticket.CHILD),
                new ReservationSeat(SeatId.of(1, 2), Ticket.STUDENT),
                new ReservationSeat(SeatId.of(1, 3), Ticket.ADULT)
        );
    }

    public static Optional<ScreeningInfo> screening(UUID screeningUuid, Instant screeningStart) {
        return Optional.of(new ScreeningInfo(screeningUuid, room1(), seats(), screeningStart));
    }

    public static Set<Seat> seats() {
        return Set.of(
                new Seat(SeatId.of(0, 0), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(0, 1), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(0, 2), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(0, 3), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(1, 0), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(1, 1), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(1, 2), SeatStatus.AVAILABLE),
                new Seat(SeatId.of(1, 3), SeatStatus.AVAILABLE)
        );
    }

    public static Name name(String name) {
        return Name.of(name);
    }

    public static Surname surname(String surname) {
        return Surname.of(surname);
    }

    public static Email email(String email) {
        return Email.of(email);
    }

    public static Room room1() {
        return Room.of(UUID.fromString("885229ac-bdd8-11e9-9cb5-2a2ae2dbcce4"));
    }

    public static UUID anyUuid() {
        return UUID.randomUUID();
    }

}
