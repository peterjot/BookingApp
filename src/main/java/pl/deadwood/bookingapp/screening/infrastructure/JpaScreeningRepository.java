package pl.deadwood.bookingapp.screening.infrastructure;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import pl.deadwood.bookingapp.screening.domain.Screening;
import pl.deadwood.bookingapp.screening.domain.ScreeningRepository;
import pl.deadwood.bookingapp.screening.domain.dto.AvailableScreening;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Repository
@AllArgsConstructor
public class JpaScreeningRepository implements ScreeningRepository {

    private final EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public Set<Screening> find(@NonNull Instant start, @NonNull Instant end) {
        Query query = em.createQuery("select s from ScreeningEntity s where s.start > :start and s.end < :end");
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<ScreeningEntity> result = query.getResultList();
        return result.stream()
                .map(ScreeningEntity::toDomain)
                .collect(toSet());
    }

    @Override
    public Optional<Screening> findById(@NonNull UUID id) {
        ScreeningEntity screeningEntity = em.find(ScreeningEntity.class, id);
        return Optional.ofNullable(screeningEntity)
                .map(ScreeningEntity::toDomain);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<AvailableScreening> findAll() {
        Query all = em.createQuery("select s from ScreeningEntity s");
        List<ScreeningEntity> result = all.getResultList();
        return result.stream()
                .map(ScreeningEntity::toAvailableDomain)
                .collect(toSet());
    }

    @Override
    @Transactional
    public void save(@NonNull Screening screening) {
        ScreeningEntity entity = new ScreeningEntity(screening);
        em.merge(entity);
    }
}
