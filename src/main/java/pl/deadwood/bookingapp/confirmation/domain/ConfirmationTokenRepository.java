package pl.deadwood.bookingapp.confirmation.domain;

import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository {

    Optional<ConfirmationToken> find(@NonNull UUID token);


    ConfirmationToken save(@NonNull ConfirmationToken confirmationToken);

    void delete(@NonNull ConfirmationToken confirmationToken);
}
