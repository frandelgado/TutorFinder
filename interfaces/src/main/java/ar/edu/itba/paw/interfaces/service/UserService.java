package ar.edu.itba.paw.interfaces.service;
import ar.edu.itba.paw.models.User;

public interface UserService {

    User findUserById(long id);
}
