package pl.deadwood.bookingapp.screening.domain;

import lombok.NonNull;

public interface ScreeningEventPublisher {

    void publish(@NonNull OnSeatsReleasedEvent seatsReleasedEvent);
}
