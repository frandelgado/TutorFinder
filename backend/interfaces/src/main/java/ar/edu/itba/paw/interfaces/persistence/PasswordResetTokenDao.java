package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PasswordResetToken;
import org.joda.time.LocalDateTime;

public interface PasswordResetTokenDao {

    PasswordResetToken findByToken(final String token);

    PasswordResetToken create(final Long userId, final String token, final LocalDateTime expires);

    void purgeExpiredTokens();

    void deleteUsedToken(final String token);
}
