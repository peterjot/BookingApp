package pl.deadwood.bookingapp.reservation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import pl.deadwood.bookingapp.reservation.domain.Email;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Surname;
import pl.deadwood.bookingapp.screening.domain.SeatId;

import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Value
@AllArgsConstructor
public class ReservationCommand {

    @NonNull
    UUID screeningId;

    @NonNull
    Set<ReservationSeat> reservationSeats;

    @NonNull
    Name name;

    @NonNull
    Surname surname;

    @NonNull
    Email email;


    public ReservationCommand(UUID screeningId,
                              Set<ReservationSeat> reservationSeats,
                              String name,
                              String surname,
                              String email) {
        this(screeningId, reservationSeats, new Name(name), new Surname(surname), new Email(email));
    }

    public Set<SeatId> getSeatIds() {
        return reservationSeats
                .stream()
                .map(ReservationSeat::getId)
                .collect(toSet());
    }
}
