package pl.deadwood.bookingapp.confirmation.domain;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Value
@AllArgsConstructor
public class ConfirmationToken {

    private static final int MINUTES_TO_EXPIRE = 60;

    UUID token;
    UUID reservationID;
    Instant expireTime;

    private ConfirmationToken(UUID reservationID, Instant now) {
        this.token = UUID.randomUUID();
        this.reservationID = reservationID;
        this.expireTime = now.plus(MINUTES_TO_EXPIRE, ChronoUnit.MINUTES);
    }

    boolean notExpired(@NonNull Instant now) {
        return now.isBefore(expireTime);
    }

    static ConfirmationToken of(@NonNull UUID reservationID, @NonNull Instant now) {
        return new ConfirmationToken(reservationID, now);
    }
}
