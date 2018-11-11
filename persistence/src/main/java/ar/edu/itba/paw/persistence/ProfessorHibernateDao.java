package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorHibernateDao implements ProfessorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Professor create(final User user, final String description, final byte[] picture) {

        final boolean exists = em.find(User.class, user.getId()) != null;

        final Professor professor = new Professor(user.getId(), user.getUsername(), user.getName(),
                user.getLastname(), user.getPassword(), user.getEmail(), description, picture);

        if(exists) {
            em.createNativeQuery("insert into professors(user_id, description, profile_picture)" +
                    " values (?, ?, ?)")
                    .setParameter(1, user.getId())
                    .setParameter(2, description)
                    .setParameter(3, picture)
                    .executeUpdate();
        } else {
            em.persist(professor);
        }
        return professor;
    }

    @Override
    public Professor modify(Professor professor, String description, byte[] picture) {
        em.merge(professor);
        if(description != null){
            professor.setDescription(description);
        }
        if(picture != null){
            professor.setPicture(picture);
        }
        return professor;
    }

    @Override
    public Optional<Professor> findById(Long professor_id) {
        final Professor professor = em.find(Professor.class, professor_id);
        return Optional.ofNullable(professor);
    }

    @Override
    public Optional<Professor> findByUsername(final String username) {
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where p.username = :username",
                Professor.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Professor> filterByFullName(final String fullName, final int limit, final int offset) {
        final String search = "%" + fullName + "%";
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where upper(concat(p.name, ' ', p.lastname)) like upper(:name)" +
                "order by p.id", Professor.class);
        query.setParameter("name", search);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
