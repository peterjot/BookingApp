package pl.deadwood.bookingapp.screening.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Seat {

    @NonNull
    private final SeatId seatId;

    @NonNull
    private SeatStatus status = SeatStatus.AVAILABLE;

    void reserveSeat() {
        status = SeatStatus.OCCUPIED;
    }

    void release() {
        status = SeatStatus.AVAILABLE;
    }

    boolean isAvailable() {
        return SeatStatus.AVAILABLE == status;
    }

    pl.deadwood.bookingapp.screening.domain.dto.Seat toDto() {
        return new pl.deadwood.bookingapp.screening.domain.dto.Seat(getSeatId(), status);
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
}
