package pl.deadwood.bookingapp.screening.domain;


import lombok.NonNull;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ScreeningRepository {

    Set<Screening> find(@NonNull Instant start, @NonNull Instant end);

    Optional<Screening> findById(@NonNull UUID id);

    Set<AvailableScreening> findAll();

    void save(@NonNull Screening screening);
}
