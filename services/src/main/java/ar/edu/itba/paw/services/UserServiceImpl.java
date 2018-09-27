package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public User findUserById(long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return userDao.findByUsername(username).orElse(null);
    }

    //TODO: Make transactional so that if the email is not sent the user is not created.
    @Override
    public User findByEmail(final String email) {
        if(email == null || email.isEmpty()) {
            return null;
        }
        return userDao.findByEmail(email).orElse(null);
    }

    @Override
    public User create(final String username, final String password, final String email,
                       final String name, final String lastName) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {

        if(username == null || password == null || email == null || name == null || lastName == null) {
            return null;
        }

        if(username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty() || lastName.isEmpty()){
            return null;
        }
        if(!name.matches("[a-zA-Z]+") && !lastName.matches("[a-zA-Z]+")){
            return null;
        }
        if(!email.matches("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")){
            return null;
        }

        User user = userDao.create(username, encoder.encode(password), email, name, lastName);

        if (user != null){
            emailService.sendRegistrationEmail(user.getEmail());
        }
        return user;
    }

    @Override
    public boolean changePassword(Long userId, String newPassword) {
        if(userId == null || newPassword == null)
            return false;
        return userDao.changePasswordById(userId, encoder.encode(newPassword));
    }
}
