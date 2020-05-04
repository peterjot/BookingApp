package pl.deadwood.bookingapp.reservation.infrastructure;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import pl.deadwood.bookingapp.reservation.domain.Name;
import pl.deadwood.bookingapp.reservation.domain.NewReservation;
import pl.deadwood.bookingapp.reservation.domain.Reservation;
import pl.deadwood.bookingapp.reservation.domain.ReservationRepository;
import pl.deadwood.bookingapp.reservation.domain.Surname;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static pl.deadwood.bookingapp.reservation.infrastructure.ReservationEntity.toDomain;

@Repository
@AllArgsConstructor
public class JpaReservationRepository implements ReservationRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    @Transactional
    public Reservation save(@NonNull NewReservation newReservation) {
        var reservation = Reservation.of(newReservation);
        var entity = new ReservationEntity(reservation);
        return em.merge(entity).toDomain();
    }

    @Override
    @Transactional
    public void update(@NonNull Reservation reservation) {
        var entity = new ReservationEntity(reservation);
        em.merge(entity);
    }

    @Override
    @Transactional
    public void update(@NonNull Set<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            update(reservation);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Reservation> find(@NonNull Name name, @NonNull Surname surname) {
        Query query = em.createQuery("select r from ReservationEntity r where r.name = :name and r.surname = :surname");
        query.setParameter("name", name.getValue());
        query.setParameter("surname", surname.getValue());
        List<ReservationEntity> result = query.getResultList();
        return toDomain(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Reservation> findByScreeningId(@NonNull UUID screeningId) {
        Query query = em.createQuery("select r from ReservationEntity r where r.screeningId = :screeningId");
        query.setParameter("screeningId", screeningId);
        List<ReservationEntity> result = query.getResultList();
        return toDomain(result);
    }


    @Override
    public Optional<Reservation> findById(@NonNull UUID id) {
        ReservationEntity reservationEntity = em.find(ReservationEntity.class, id);
        return Optional.ofNullable(reservationEntity)
                .map(ReservationEntity::toDomain);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Reservation> findByTimeBefore(@NonNull Instant now) {
        Query query = em.createQuery("select r from ReservationEntity r where r.expireTime < :now");
        query.setParameter("now", now);
        List<ReservationEntity> result = query.getResultList();
        return toDomain(result);
    }
}
