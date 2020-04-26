package pl.deadwood.bookingapp.screening.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;
import pl.deadwood.bookingapp.screening.domain.dto.ScreeningInfo;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toCollection;

@AllArgsConstructor
public class Screenings {

    private final ScreeningRepository screeningRepository;
    private final ScreeningEventPublisher screeningEventPublisher;


    public Set<AvailableScreening> find(@NonNull Instant from, @NonNull Instant to) {
        return screeningRepository
                .find(from, to)
                .stream()
                .map(Screening::toAvailableScreeningDto)
                .collect(toSortedByTitleAndStartTimeSet());
    }

    public Optional<ScreeningInfo> find(@NonNull UUID uuid) {
        return screeningRepository
                .findById(uuid)
                .map(Screening::toDto);
    }

    public Set<AvailableScreening> findAll() {
        return screeningRepository.findAll();
    }

    public void reserveSeats(@NonNull UUID screeningId, @NonNull Set<SeatId> seatIds) {
        Screening availableScreening = getScreening(screeningId);
        availableScreening.reserveSeats(seatIds);
        screeningRepository.save(availableScreening);
    }

    private Screening getScreening(@NonNull UUID screeningId) {
        return screeningRepository.findById(screeningId).orElseThrow(IllegalStateException::new);
    }

    public void releaseSeats(@NonNull UUID screeningId, @NonNull Set<SeatId> seatIds) {
        Screening screening = getScreening(screeningId);
        screening.reserveSeats(seatIds);
        screeningRepository.save(screening);
        publishSeatsReleasedEvent(screeningId, seatIds);
    }

    private Collector<AvailableScreening, ?, TreeSet<AvailableScreening>> toSortedByTitleAndStartTimeSet() {
        return toCollection(() -> new TreeSet<>(AvailableScreening.ORDER_BY_TITLE_AND_START_TIME));
    }

    private void publishSeatsReleasedEvent(@NonNull UUID screeningId, @NonNull Set<SeatId> seatIds) {
        screeningEventPublisher.publish(new OnSeatsReleasedEvent(screeningId, seatIds));
    }
}
