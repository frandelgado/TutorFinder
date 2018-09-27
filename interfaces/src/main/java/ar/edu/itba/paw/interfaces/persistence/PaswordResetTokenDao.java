package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.PasswordResetToken;
import org.joda.time.LocalDateTime;

public interface PaswordResetTokenDao {

    PasswordResetToken findByToken(final String token);

    PasswordResetToken create(final Long userId, final String token, final LocalDateTime expires);
}
