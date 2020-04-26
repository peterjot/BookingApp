package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;

public interface EventPublisher {

    void publish(@NonNull OnReservationCreatedEvent event);
}
