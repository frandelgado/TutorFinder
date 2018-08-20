package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.User;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserById(long id) {
        return this.userDao.findById(id);
    }
}
