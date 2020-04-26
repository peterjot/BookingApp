package pl.deadwood.bookingapp.reservation.domain;


import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class OnReservationCreatedEvent {

    @NonNull
    UUID reservationId;

    @NonNull
    Email email;
}
