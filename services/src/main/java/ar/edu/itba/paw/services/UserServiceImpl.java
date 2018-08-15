package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;

public class UserServiceImpl implements UserService {
    
    @Override
    public User findUserById(long id) {
        return new User();
    }
}
