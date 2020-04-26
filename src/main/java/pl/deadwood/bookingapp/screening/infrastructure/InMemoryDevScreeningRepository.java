package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.NonNull;
import pl.deadwood.bookingapp.screening.domain.Screening;
import pl.deadwood.bookingapp.screening.domain.ScreeningRepository;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toSet;

public class InMemoryDevScreeningRepository implements ScreeningRepository {

    private final Map<UUID, Screening> database = new ConcurrentHashMap<>();


    @Override
    public Set<Screening> find(@NonNull Instant start, @NonNull Instant end) {
        return database
                .values()
                .stream()
                .filter(screening -> screening.getStart().isAfter(start) && screening.getEnd().isBefore(end))
                .collect(toSet());
    }

    @Override
    public Optional<Screening> findById(@NonNull UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Set<AvailableScreening> findAll() {
        return database.values()
                .stream()
                .map(screening -> new AvailableScreening(screening.getId(), screening.getMovie(), screening.getStart()))
                .collect(toSet());
    }

    @Override
    public void save(@NonNull Screening screening) {
        database.put(screening.getId(), screening);
    }
}
