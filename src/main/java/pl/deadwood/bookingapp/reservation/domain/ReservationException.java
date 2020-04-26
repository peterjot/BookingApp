package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;

public class ReservationException extends RuntimeException {

    ReservationException(@NonNull String error) {
        super(error);
    }
}
