package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class Room {

    @NonNull
    UUID id;
}
