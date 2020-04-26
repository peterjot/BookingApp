package pl.deadwood.bookingapp.reservation.domain.dto;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
public class CreatedReservation {

    @NonNull
    UUID reservationId;

    @NonNull
    BigDecimal amountToPay;

    @NonNull
    Instant expireTime;
}
