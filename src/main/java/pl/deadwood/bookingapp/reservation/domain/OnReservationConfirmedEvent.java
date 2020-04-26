package pl.deadwood.bookingapp.reservation.domain;


import java.util.UUID;

@FunctionalInterface
public interface OnReservationConfirmedEvent {
    UUID getReservationId();
}
