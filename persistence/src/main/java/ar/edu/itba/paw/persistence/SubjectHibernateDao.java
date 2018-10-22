package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SubjectDao;
import ar.edu.itba.paw.models.Subject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class SubjectHibernateDao implements SubjectDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Subject> findById(final long id) {
        final Subject subject = em.find(Subject.class, id);
        return Optional.ofNullable(subject);
    }

    @Override
    public Subject create(final String name, final String description, final Long area_id) {
        final Subject subject = new Subject(description, name, null);
        em.persist(subject);
        return subject;
    }

    @Override
    public List<Subject> filterSubjectsByName(final String name) {
        final String search = "%" + name + "%";
        final TypedQuery<Subject> query = em.createQuery("from Subject as s where upper(s.name) like upper(:name)"
                , Subject.class);
        query.setParameter("name", search);
        return query.getResultList();
    }

    public List<Subject> getAvailableSubjects(final long id) {
        final TypedQuery<Subject> query = em.createQuery("from Subject as s where not exists (from Course as c where" +
                        " c.subject = s AND c.professor.id = :id)", Subject.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
