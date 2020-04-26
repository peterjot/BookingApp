package pl.deadwood.bookingapp.screening.domain.dto;

import lombok.NonNull;
import lombok.Value;
import pl.deadwood.bookingapp.screening.domain.Room;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Value
public class ScreeningInfo {

    @NonNull
    UUID id;

    @NonNull
    Room room;

    @NonNull
    Set<Seat> seats;

    @NonNull
    Instant start;
}
