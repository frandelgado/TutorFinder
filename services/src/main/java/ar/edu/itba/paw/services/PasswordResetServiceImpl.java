package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTokenException;
import ar.edu.itba.paw.interfaces.persistence.PasswordResetTokenDao;
import ar.edu.itba.paw.interfaces.service.PasswordResetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Integer EXPIRATION = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenDao passwordResetTokenDao;

    @Transactional
    @Override
    public boolean createToken(final String email) {

        final User user = userService.findByEmail(email);
        if(user == null) {
            return false;
        }

        final String token = UUID.randomUUID().toString();
        final PasswordResetToken passwordResetToken = passwordResetTokenDao.create(user.getId(),
                token, LocalDateTime.now().plusDays(EXPIRATION));

        //TODO: SEND MAIL

        return true;
    }

    @Override
    @Scheduled(fixedRate = 24*60*60*1000) //24 Hours
    public void purgeExpired() {
        passwordResetTokenDao.purgeExpiredTokens();
    }

    @Override
    public PasswordResetToken findByToken(final String token) {

        if(token == null || token.isEmpty()) {
            return null;
        }

        return passwordResetTokenDao.findByToken(token);
    }

    @Override
    public void deleteUsedToken(final String token) {
        if(token != null && !token.isEmpty())
            passwordResetTokenDao.deleteUsedToken(token);
    }

    @Transactional
    @Override
    public User changePassword(final String token, final String password) throws InvalidTokenException {

        final PasswordResetToken passwordResetToken = findByToken(token);

        if(passwordResetToken == null) {
            throw new InvalidTokenException();
        }

        final User user = passwordResetToken.getUser();
        final boolean changed = userService.changePassword(user.getId(), password);
        if(changed) {
            deleteUsedToken(passwordResetToken.getToken());
            return user;
        }
        return null;
    }

}
