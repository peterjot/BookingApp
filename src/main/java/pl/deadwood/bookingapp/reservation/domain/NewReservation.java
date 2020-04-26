package pl.deadwood.bookingapp.reservation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.deadwood.bookingapp.reservation.domain.dto.ReservationCommand;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "screeningId")
public class NewReservation {

    private final UUID screeningId;
    private final Set<ReservationSeat> reservationSeats;
    private final Name name;
    private final Surname surname;
    private final Email email;
    private final Instant expireTime;

    public NewReservation(@NonNull UUID screeningId,
                          @NonNull Set<ReservationSeat> reservationSeats,
                          @NonNull Name name,
                          @NonNull Surname surname,
                          @NonNull Email email,
                          @NonNull Instant expireTime) {
        this.screeningId = screeningId;
        this.reservationSeats = new HashSet<>(reservationSeats);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.expireTime = expireTime;
    }

    public static NewReservation of(ReservationCommand reservationCommand, Instant expireTime) {

        return new NewReservation(reservationCommand.getScreeningId(),
                reservationCommand.getReservationSeats(),
                reservationCommand.getName(),
                reservationCommand.getSurname(),
                reservationCommand.getEmail(),
                expireTime);
    }
}
