package pl.deadwood.bookingapp.reservation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import pl.deadwood.bookingapp.reservation.domain.EventPublisher;
import pl.deadwood.bookingapp.reservation.domain.OnReservationCreatedEvent;

@AllArgsConstructor
public class SpringEventPublisher implements EventPublisher {


    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(@NonNull OnReservationCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
