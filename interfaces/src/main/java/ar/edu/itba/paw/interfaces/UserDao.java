package ar.edu.itba.paw.persistance;
//TODO: estos paquetes deberian ser de  interfaces.persistance y no solamente persistance no?
import ar.edu.itba.paw.model.User;

public interface UserDao {

    User findById(long id);
}
