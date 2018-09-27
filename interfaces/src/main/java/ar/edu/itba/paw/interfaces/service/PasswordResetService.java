package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.InvalidTokenException;
import ar.edu.itba.paw.models.PasswordResetToken;

public interface PasswordResetService {

    boolean createToken(final String email);

    void purgeExpired();

    PasswordResetToken findByToken(final String token);

    void deleteUsedToken(final String token);

    boolean changePassword(PasswordResetToken token, String password) throws InvalidTokenException;
}
