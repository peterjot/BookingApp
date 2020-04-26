package pl.deadwood.bookingapp.screening.domain.dto;

import lombok.NonNull;
import lombok.Value;
import pl.deadwood.bookingapp.screening.domain.SeatId;
import pl.deadwood.bookingapp.screening.domain.SeatStatus;

@Value
public class Seat {

    @NonNull
    SeatId id;

    @NonNull
    SeatStatus status;

    public boolean available() {
        return SeatStatus.AVAILABLE == status;
    }
}
