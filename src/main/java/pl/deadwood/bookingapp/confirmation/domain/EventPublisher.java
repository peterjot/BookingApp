package pl.deadwood.bookingapp.confirmation.domain;

import lombok.NonNull;
import pl.deadwood.bookingapp.reservation.domain.OnReservationConfirmedEvent;

public interface EventPublisher {

    void publish(@NonNull OnReservationConfirmedEvent event);
}
