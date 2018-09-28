package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.InvalidTokenException;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;

public interface PasswordResetService {

    boolean createToken(final String email);

    void purgeExpired();

    PasswordResetToken findByToken(final String token);

    void deleteUsedToken(final String token);

    User changePassword(String token, String password) throws InvalidTokenException;
}
