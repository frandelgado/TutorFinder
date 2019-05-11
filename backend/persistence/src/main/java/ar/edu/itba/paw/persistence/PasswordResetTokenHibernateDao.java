package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PasswordResetTokenDao;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PasswordResetTokenHibernateDao implements PasswordResetTokenDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetTokenHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public PasswordResetToken findByToken(final String token) {
        LOGGER.trace("Querying for password reset token: {}", token);
        final TypedQuery<PasswordResetToken> query = em.createQuery("from PasswordResetToken as t " +
                "where t.token = :token and t.expireDate >= NOW()", PasswordResetToken.class);
        query.setParameter("token", token);
        final List<PasswordResetToken> tokens = query.getResultList();
        return tokens.isEmpty()? null : tokens.get(0);
    }

    @Override
    public PasswordResetToken create(final Long userId, final String token, final LocalDateTime expires) {
        LOGGER.trace("Inserting password reset token for user with id {}", userId);
        final User user = em.getReference(User.class, userId);
        final PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expires);
        em.persist(passwordResetToken);
        try {
            em.flush();
        } catch (PersistenceException e) {
            LOGGER.error("User with id {} doesn't exist", userId);
            LOGGER.warn("Password reset token for user with id {} was not created", userId);
            return null;
        }
        return passwordResetToken;
    }

    @Override
    public void purgeExpiredTokens() {
        LOGGER.trace("Deleting expired tokens");
        em.createQuery("delete from PasswordResetToken as t where t.expireDate < current_timestamp()")
                .executeUpdate();
    }

    @Override
    public void deleteUsedToken(final String token) {
        LOGGER.trace("Deleting used token: {}", token);
        em.createQuery("delete from PasswordResetToken as t where t.token = :token")
                .setParameter("token", token)
                .executeUpdate();
    }
}
