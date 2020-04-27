package pl.deadwood.bookingapp.confirmation.infrastructure;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationToken;
import pl.deadwood.bookingapp.confirmation.domain.ConfirmationTokenRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JpaConfirmationTokenRepository implements ConfirmationTokenRepository {

    private final EntityManager em;

    public JpaConfirmationTokenRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

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
    public ConfirmationToken save(@NonNull ConfirmationToken confirmationToken) {
        var entity = new ConfirmationTokenEntity(confirmationToken);
        doInTransaction(() -> em.merge(entity));
        return new ConfirmationToken(entity.getToken(), entity.getReservationID(), entity.getExpireTime());
    }

    @Override
    public void delete(@NonNull ConfirmationToken confirmationToken) {
        Query query = em.createQuery("delete from ConfirmationTokenEntity c where c.token = :token");
        query.setParameter("token", confirmationToken.getToken());
        doInTransaction(query::executeUpdate);
    }


    private void doInTransaction(Runnable runnable) {
        em.getTransaction().begin();
        try {
            runnable.run();
            em.getTransaction().commit();
        } catch (RollbackException ex) {
            em.getTransaction().rollback();
        }
    }
}
