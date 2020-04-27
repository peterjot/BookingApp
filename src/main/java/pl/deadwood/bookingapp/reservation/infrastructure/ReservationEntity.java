package pl.deadwood.bookingapp.reservation.infrastructure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.Reservation;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Status;
import pl.deadwood.bookingapp.reservation.domain.Surname;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReservationEntity {

    @Id
    private UUID id;

    @NotNull
    private UUID screeningId;

    @ElementCollection
    private Set<ReservationSeatEntity> reservationSeats;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    @NotNull
    private String email;

    @NotNull
    private Instant expireTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;


    public ReservationEntity(Reservation reservation) {
        this.screeningId = reservation.getScreeningId();
        this.reservationSeats = reservation.getReservationSeats()
                .stream()
                .map(ReservationSeatEntity::new)
                .collect(toSet());
        this.name = reservation.getName().getValue();
        this.surname = reservation.getSurname().getValue();
        this.email = reservation.getEmail().getValue();
        this.expireTime = reservation.getExpireTime();
        this.status = reservation.getStatus();
    }

    public Reservation toDomain() {
        Set<ReservationSeat> reservationSeats = this.reservationSeats
                .stream()
                .map(ReservationSeatEntity::toDomain)
                .collect(toSet());

        return new Reservation(
                id,
                screeningId,
                reservationSeats,
                new Name(name),
                new Surname(surname),
                new pl.deadwood.bookingapp.reservation.domain.Email(email),
                expireTime,
                status
        );
    }

    public static Set<Reservation> toDomain(Collection<ReservationEntity> entities) {
        return entities.stream()
                .map(ReservationEntity::toDomain)
                .collect(toSet());
    }

}
