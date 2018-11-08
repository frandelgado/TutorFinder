package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PasswordResetTokenDao;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PasswordResetTokenHibernateDao implements PasswordResetTokenDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PasswordResetToken findByToken(String token) {
        final TypedQuery<PasswordResetToken> query = em.createQuery("from PasswordResetToken as t " +
                "where t.token = :token", PasswordResetToken.class);
        query.setParameter("token", token);
        final List<PasswordResetToken> tokens = query.getResultList();
        return tokens.isEmpty()? null : tokens.get(0);
    }

    @Override
    public PasswordResetToken create(Long userId, String token, LocalDateTime expires) {
        final User user = em.getReference(User.class, userId);
        final PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expires);
        em.persist(passwordResetToken);
        return passwordResetToken;
    }

    @Override
    public void purgeExpiredTokens() {
        em.createQuery("delete from PasswordResetToken as t where t.expireDate < current_timestamp()")
                .executeUpdate();
    }

    @Override
    public void deleteUsedToken(String token) {
        em.createQuery("delete from PasswordResetToken as t where t.token = :token")
                .setParameter("token", token)
                .executeUpdate();
    }
}
