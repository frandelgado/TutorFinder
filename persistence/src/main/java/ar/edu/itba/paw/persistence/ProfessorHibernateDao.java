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
    public Professor create(User user, String description, byte[] picture) {
        final Professor professor = new Professor(user.getUsername(), user.getName(),
                user.getLastname(), user.getPassword(), user.getEmail(), description, picture);
        em.persist(professor);
        return professor;
    }

    @Override
    public Optional<Professor> findById(Long professor_id) {
        final Professor professor = em.find(Professor.class, professor_id);
        return Optional.ofNullable(professor);
    }

    @Override
    public Optional<Professor> findByUsername(String username) {
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where p.username = :username",
                Professor.class);
        query.setParameter("username", username);
        final List<Professor> professors = query.getResultList();
        return professors.isEmpty()? Optional.empty() : Optional.ofNullable(professors.get(0));
    }

    @Override
    public List<Professor> filterByFullName(String fullName, int limit, int offset) {
        final String search = "%" + fullName + "%";
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where upper(concat(p.name, ' ', p.lastname)) like upper(:name)" +
                "order by p.id", Professor.class);
        query.setParameter("name", search);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
