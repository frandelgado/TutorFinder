package ar.edu.itba.paw.interfaces;
//TODO: estos paquetes deberian ser de  interfaces.persistance y no solamente persistance no?
import ar.edu.itba.paw.interfaces.User;

public interface UserDao {

    User findById(long id);
}
