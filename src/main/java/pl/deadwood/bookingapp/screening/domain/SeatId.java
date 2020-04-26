package pl.deadwood.bookingapp.screening.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class SeatId {
    int row;
    int column;
}
