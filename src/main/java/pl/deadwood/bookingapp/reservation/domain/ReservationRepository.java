package pl.deadwood.bookingapp.reservation.domain;

import lombok.NonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ReservationRepository {

    Reservation save(@NonNull NewReservation newReservation);

    void update(@NonNull Reservation reservation);

    void update(@NonNull Set<Reservation> reservations);

    Set<Reservation> find(@NonNull Name name, @NonNull Surname surname);

    Set<Reservation> findByScreeningId(@NonNull UUID screeningId);

    Optional<Reservation> findById(@NonNull UUID id);

    Set<Reservation> findByTimeBefore(@NonNull Instant now);
}
