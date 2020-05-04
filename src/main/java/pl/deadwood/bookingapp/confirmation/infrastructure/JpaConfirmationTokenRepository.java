package pl.deadwood.bookingapp.confirmation.infrastructure;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationTokenRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor
public class JpaConfirmationTokenRepository implements ConfirmationTokenRepository {

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Optional<ConfirmationToken> find(@NonNull UUID token) {
        Query query = em.createQuery("select c from ConfirmationTokenEntity c where c.token = :token");
        query.setParameter("token", token);

        ConfirmationTokenEntity result;
        try {
            result = (ConfirmationTokenEntity) query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("Cannot find confirmation token: {} ", token);
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Unable to find confirmation token: {} ", token, e);
            return Optional.empty();
        }
        return Optional.of(new ConfirmationToken(result.getToken(), result.getReservationID(), result.getExpireTime()));
    }

    @Override
    @Transactional
    public ConfirmationToken save(@NonNull ConfirmationToken confirmationToken) {
        var entity = em.merge(new ConfirmationTokenEntity(confirmationToken));
        return new ConfirmationToken(entity.getToken(), entity.getReservationID(), entity.getExpireTime());
    }

    @Override
    @Transactional
    public void delete(@NonNull ConfirmationToken confirmationToken) {
        Query query = em.createQuery("delete from ConfirmationTokenEntity c where c.token = :token");
        query.setParameter("token", confirmationToken.getToken());
        query.executeUpdate();
    }
}
