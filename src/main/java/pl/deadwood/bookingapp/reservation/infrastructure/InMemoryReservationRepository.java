package pl.deadwood.bookingapp.reservation.infrastructure;

import lombok.NonNull;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.NewReservation;
import pl.deadwood.bookingapp.reservation.domain.Reservation;
import pl.deadwood.bookingapp.reservation.domain.ReservationRepository;
import pl.deadwood.bookingapp.reservation.domain.Surname;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<UUID, Reservation> database = new ConcurrentHashMap<>();


    @Override
    public Reservation save(@NonNull NewReservation newReservation) {
        Reservation reservation = Reservation.of(newReservation);
        database.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public void update(@NonNull Reservation reservation) {
        database.put(reservation.getId(), reservation);
    }

    @Override
    public void update(@NonNull Set<Reservation> reservations) {
        database.putAll(
                reservations
                        .stream()
                        .collect(toMap(Reservation::getId, Function.identity())));
    }

    @Override
    public Optional<Reservation> findById(@NonNull UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Set<Reservation> find(@NonNull Name name, @NonNull Surname surname) {
        return database
                .values()
                .stream()
                .filter(byNameAndSurname(name, surname))
                .collect(toSet());
    }

    @Override
    public Set<Reservation> findByScreeningId(@NonNull UUID screeningId) {
        return database
                .values()
                .stream()
                .filter(reservation -> reservation.getScreeningId().equals(screeningId))
                .collect(toSet());
    }

    @Override
    public Set<Reservation> findByTimeBefore(Instant now) {
        return database
                .values()
                .stream()
                .filter(reservation -> reservation.getExpireTime().isBefore(now))
                .collect(toSet());
    }

    private Predicate<Reservation> byNameAndSurname(Name name, Surname surname) {
        return reservation -> reservation.getName().equals(name) && reservation.getSurname().equals(surname);
    }
}
