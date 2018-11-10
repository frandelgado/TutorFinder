package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
public class CourseHibernateDao implements CourseDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Course> findByIds(final long professor_id, final long subject_id) {
        final TypedQuery<Course> query = em.createQuery("from Course as c " +
                "where c.professor.id = :professor and c.subject.id = :subject", Course.class);
        query.setParameter("professor", professor_id);
        query.setParameter("subject", subject_id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Course> findByProfessorId(final long professor_id, final int limit, final int offset) {
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.professor.id = :id " +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", professor_id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filterByAreaId(final long areaId, final int limit, final int offset) {
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.subject.area.id = :id " +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", areaId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filter(final Filter filter, final int limit, final int offset) {
        TypedQuery<Course> query = em.createQuery(filter.getQuery(), Course.class);
        List<Object> params = filter.getQueryParams();
        IntStream.range(0, params.size())
                .forEach(i-> query.setParameter(i+1, params.get(i)));

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Course create(final Professor professor, final Subject subject, final String description, final Double price) throws CourseAlreadyExistsException {
        final Course course = new Course(professor, subject, description, price);
        try {
            em.persist(course);
            em.flush();
            return course;
        }
        //TODO specify exception
        catch (PersistenceException e) {
            throw new CourseAlreadyExistsException();
        }
    }
}
