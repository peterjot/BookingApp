package pl.deadwood.bookingapp.reservation.application;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import pl.deadwood.bookingapp.reservation.domain.OnReservationConfirmedEvent;
import pl.deadwood.bookingapp.reservation.domain.Reservations;
import pl.deadwood.bookingapp.screening.domain.OnSeatsReleasedEvent;

@AllArgsConstructor
class EventHandler {

    @NonNull
    private final Reservations reservations;

    @EventListener
    public void onSeatsReleased(OnSeatsReleasedEvent event) {
        reservations.releaseSeats(event.getScreeningId(), event.getSeatIds());
    }

    @EventListener
    public void onReservationConfirmed(OnReservationConfirmedEvent event) {
        reservations.confirmReservation(event.getReservationId());
    }
}
