package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.model.User;


//TODO: Falta la dependencia de spring
@Repository
public class UserJdbcDao implements UserDao {
    @Override
    public User findById(long id) {
        return null;
    }
}
