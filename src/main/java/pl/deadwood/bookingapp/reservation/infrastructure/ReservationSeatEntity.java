package pl.deadwood.bookingapp.reservation.infrastructure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.reservation.domain.ReservationSeat;
import pl.deadwood.bookingapp.reservation.domain.Ticket;
import pl.deadwood.bookingapp.screening.domain.SeatId;
import pl.deadwood.bookingapp.screening.infrastructure.SeatIdEntity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "seatId")
public class ReservationSeatEntity {

    @Embedded
    private SeatIdEntity seatId;

    @NotNull
    @Enumerated(EnumType.STRING)
    Ticket ticket;

    public ReservationSeatEntity(ReservationSeat reservationSeat) {
        SeatId seatId = reservationSeat.getId();
        this.seatId = new SeatIdEntity(seatId);
        this.ticket = reservationSeat.getTicket();
    }

    public ReservationSeat toDomain() {
        return new ReservationSeat(seatId.toDomain(), ticket);
    }
}
