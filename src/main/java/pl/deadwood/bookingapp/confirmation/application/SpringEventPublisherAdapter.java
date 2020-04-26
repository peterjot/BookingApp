package pl.deadwood.bookingapp.confirmation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import pl.deadwood.bookingapp.confirmation.domain.EventPublisher;
import pl.deadwood.bookingapp.reservation.domain.OnReservationConfirmedEvent;

@AllArgsConstructor
public class SpringEventPublisherAdapter implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(@NonNull OnReservationConfirmedEvent reservationConfirmedEvent) {
        applicationEventPublisher.publishEvent(reservationConfirmedEvent);
    }
}
