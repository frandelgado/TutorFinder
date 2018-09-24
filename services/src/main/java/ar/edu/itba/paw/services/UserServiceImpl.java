package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
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

    @Override
    public User create(final String username, final String password, final String email,
                       final String name, final String lastName) {

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
        //TODO: THROW EXCEPTION UPWARDS TO HAVE CONTROLLER DISPLAY ERRORS
        try {
            return userDao.create(username, encoder.encode(password), email, name, lastName);
        } catch (Exception e) {
            return null;
        }
    }
}
