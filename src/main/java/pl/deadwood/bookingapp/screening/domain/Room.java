package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class Room {

    @NonNull
    UUID id;
}
