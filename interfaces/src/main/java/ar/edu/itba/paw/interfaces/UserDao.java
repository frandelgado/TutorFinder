package ar.edu.itba.paw.interfaces;
//TODO: estos paquetes deberian ser de  interfaces.persistence y no solamente persistence no?
import ar.edu.itba.paw.interfaces.User;

public interface UserDao {

    User findById(long id);
}
