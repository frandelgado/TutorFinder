package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTokenException;
import ar.edu.itba.paw.exceptions.TokenCrationException;
import ar.edu.itba.paw.interfaces.persistence.PasswordResetTokenDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.PasswordResetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Integer EXPIRATION = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenDao passwordResetTokenDao;

    @Autowired
    private EmailService emailService;

    @Transactional(rollbackFor = TokenCrationException.class)
    @Override
    public boolean createToken(final String email) throws TokenCrationException {

        final User user = userService.findByEmail(email);
        if(user == null) {
            LOGGER.error("Attempted to create password reset token for non registered email");
            return false;
        }

        LOGGER.debug("Creating password reset token for user with id {}", user.getId());
        final String token = UUID.randomUUID().toString();
        final PasswordResetToken passwordResetToken = passwordResetTokenDao.create(user.getId(),
                token, LocalDateTime.now().plusDays(EXPIRATION));

        if(passwordResetToken != null) {
            final boolean created = emailService.sendRestorePasswordEmail(user, passwordResetToken.getToken());
            if(!created) {
                throw new TokenCrationException();
            }
        }

        return true;
    }

    @Override
    @Scheduled(fixedRate = 24*60*60*1000) //24 Hours
    public void purgeExpired() {
        LOGGER.debug("Purging expired tokens");
        passwordResetTokenDao.purgeExpiredTokens();
    }

    @Override
    public PasswordResetToken findByToken(final String token) {

        if(token == null || token.isEmpty()) {
            LOGGER.error("Attempted to find password reset token with empty token");
            return null;
        }

        LOGGER.debug("Searching for password reset token with token {}", token);
        return passwordResetTokenDao.findByToken(token);
    }

    @Override
    public void deleteUsedToken(final String token) {
        if(token != null && !token.isEmpty()) {
            LOGGER.debug("Deleting used password reset token: {}", token);
            passwordResetTokenDao.deleteUsedToken(token);
        }
    }

    @Transactional
    @Override
    public User changePassword(final String token, final String password) throws InvalidTokenException {

        final PasswordResetToken passwordResetToken = findByToken(token);

        if(passwordResetToken == null) {
            LOGGER.error("Attempted to change password using an invalid token");
            throw new InvalidTokenException();
        }

        final User user = passwordResetToken.getUser();
        LOGGER.debug("Changing password for user with id {} using token {}", user.getId(), token);
        final boolean changed = userService.changePassword(user.getId(), password);
        if(changed) {
            deleteUsedToken(passwordResetToken.getToken());
            return user;
        }
        return null;
    }

}
