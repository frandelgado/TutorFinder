package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findByEmail(final String email);

    User create(final String username, final String password, final String email,
                final String name, final String lastName)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException;

    Optional<User> findByUsername(final String username);

    List<ClassReservation> pagedReservations(final Long userId, final Integer limit, final Integer offset);

    long totalReservations(Long userId);

    boolean changePasswordById(final Long userId, final String newPassword);
}
