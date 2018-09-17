package ar.edu.itba.paw.interfaces.service;
import ar.edu.itba.paw.models.User;

public interface UserService {

    User findUserById(final long id);

    User findByUsername(final String username);

    User create(final String username, final String password,
                final String email, final String name, final String lastName);
}
