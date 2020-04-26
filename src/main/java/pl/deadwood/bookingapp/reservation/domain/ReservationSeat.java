package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;
import lombok.Value;
import pl.deadwood.bookingapp.screening.domain.SeatId;

@Value
public class ReservationSeat {

    @NonNull
    SeatId id;

    @NonNull
    Ticket ticket;
}
