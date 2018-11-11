package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findById(final long id) {
        User us =  em.find(User.class, id);
        return Optional.ofNullable(us);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public User create(final String username, final String password, final String email, final String name, final String lastName) throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final User user = new User(username, name, lastName, password, email);
        em.persist(user);
        return user;
    }

    //TODO: podriamos hacer que no retorne un optional
    @Override
    public Optional<User> findByUsername(final String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<ClassReservation> pagedReservations(User user, Integer limit, Integer offset) {
        final TypedQuery<ClassReservation> query = em.createQuery("from ClassReservation as c where c.student = :student " +
                "order by c.classRequestId", ClassReservation.class);
        query.setParameter("student", user);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public boolean changePasswordById(final Long userId, final String newPassword) {
        final User user = em.find(User.class, userId);

        if(user == null) {
            return false;
        }

        user.setPassword(newPassword);
        return em.merge(user) != null;
    }
}
