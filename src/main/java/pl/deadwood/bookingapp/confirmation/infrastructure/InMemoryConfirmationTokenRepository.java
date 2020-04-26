package pl.deadwood.bookingapp.confirmation.infrastructure;

import lombok.NonNull;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationTokenRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryConfirmationTokenRepository implements ConfirmationTokenRepository {

    private final Map<UUID, ConfirmationToken> database = new ConcurrentHashMap<>();

    @Override
    public Optional<ConfirmationToken> find(@NonNull UUID token) {
        return Optional.ofNullable(database.get(token));
    }

    @Override
    public ConfirmationToken save(@NonNull ConfirmationToken confirmationToken) {
        database.put(confirmationToken.getToken(), confirmationToken);
        return confirmationToken;
    }

    @Override
    public void delete(@NonNull ConfirmationToken confirmationToken) {
        database.remove(confirmationToken.getToken());
    }
}
