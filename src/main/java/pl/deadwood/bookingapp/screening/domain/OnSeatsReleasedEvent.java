package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class OnSeatsReleasedEvent {

    @NonNull
    UUID screeningId;

    @NonNull
    Set<SeatId> seatIds;
}
