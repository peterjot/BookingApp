package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.screening.domain.SeatId;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class SeatIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int _row;
    private int _col;

    public SeatIdEntity(SeatId seatId) {
        this._row = seatId.getRow();
        this._col = seatId.getColumn();
    }

    public SeatId toDomain() {
        return SeatId.of(_row, _col);
    }
}
