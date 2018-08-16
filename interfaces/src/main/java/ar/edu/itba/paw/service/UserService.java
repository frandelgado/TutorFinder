package ar.edu.itba.paw.service;
import ar.edu.itba.paw.interfaces.User;
public interface UserService {

    User findUserById(long id);
}
