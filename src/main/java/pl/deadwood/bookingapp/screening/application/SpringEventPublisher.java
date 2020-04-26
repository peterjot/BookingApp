package pl.deadwood.bookingapp.screening.application;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import pl.deadwood.bookingapp.screening.domain.OnSeatsReleasedEvent;
import pl.deadwood.bookingapp.screening.domain.ScreeningEventPublisher;

@AllArgsConstructor
class SpringEventPublisher implements ScreeningEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void publish(@NonNull OnSeatsReleasedEvent seatsReleasedEvent) {
        applicationEventPublisher.publishEvent(seatsReleasedEvent);
    }
}
