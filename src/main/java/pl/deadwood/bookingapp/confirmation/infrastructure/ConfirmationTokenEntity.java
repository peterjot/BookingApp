package pl.deadwood.bookingapp.confirmation.infrastructure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
class ConfirmationTokenEntity {

    @Id
    private UUID token;
    private UUID reservationID;
    private Instant expireTime;

    ConfirmationTokenEntity(ConfirmationToken confirmationToken) {
        this.token = confirmationToken.getToken();
        this.reservationID = confirmationToken.getReservationID();
        this.expireTime = confirmationToken.getExpireTime();
    }
}