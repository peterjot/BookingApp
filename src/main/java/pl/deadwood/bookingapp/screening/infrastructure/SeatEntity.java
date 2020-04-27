package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.screening.domain.Seat;
import pl.deadwood.bookingapp.screening.domain.SeatId;
import pl.deadwood.bookingapp.screening.domain.SeatStatus;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
class SeatEntity implements Serializable {

    @Embedded
    @NotNull
    private SeatIdEntity seatId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    SeatEntity(Seat seat) {
        SeatId seatId = seat.getSeatId();
        this.seatId = new SeatIdEntity(seatId);
        this.status = seat.getStatus();
    }

    Seat toDomain() {
        Seat seat = new Seat(this.seatId.toDomain());
        seat.setStatus(status);
        return seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeatEntity that = (SeatEntity) o;
        return seatId.equals(that.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId);
    }
}
