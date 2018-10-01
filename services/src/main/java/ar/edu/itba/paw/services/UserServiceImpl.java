package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public User findUserById(long id) {
        LOGGER.debug("Searching for user with id {}", id);
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            LOGGER.error("Attempted to find user with empty username");
            return null;
        }
        LOGGER.debug("Searching for user with username {}", username);
        return userDao.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(final String email) {
        if(email == null || email.isEmpty()) {
            LOGGER.error("Attempted to find user with empty email");
            return null;
        }
        LOGGER.debug("Searching for user with email {}", email);
        return userDao.findByEmail(email).orElse(null);
    }

    @Override
    public User create(final String username, final String password, final String email,
                       final String name, final String lastName) throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {

        if(username == null || password == null || email == null || name == null || lastName == null) {
            LOGGER.error("Attempted to create user with empty fields");
            return null;
        }

        if(username.length() == 0 || password.length() < 8 || email.length() == 0 || name.length() == 0 || lastName.length() == 0) {
            LOGGER.error("Attempted to create user with invalid field lengths");
            return null;
        }
        if(!name.matches("[a-zA-Z]+") && !lastName.matches("[a-zA-Z]+")){
            LOGGER.error("Attempted to create user with invalid name");
            return null;
        }
        if(!email.matches("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")){
            LOGGER.error("Attempted to create user with invalid email");
            return null;
        }

        LOGGER.debug("Creating user with username {}", username);

        User user = userDao.create(username, encoder.encode(password), email, name, lastName);

        if (user != null){
            LOGGER.debug("Sending welcome mail for user with id {}", user.getId());
            emailService.sendRegistrationEmail(user);
        }
        return user;
    }

    @Override
    public boolean changePassword(Long userId, String newPassword) {
        if(userId == null || newPassword == null) {
            LOGGER.error("Attempted to change password with null parameters");
            return false;
        }
        LOGGER.debug("Changing password for user with id {}", userId);
        return userDao.changePasswordById(userId, encoder.encode(newPassword));
    }
}
