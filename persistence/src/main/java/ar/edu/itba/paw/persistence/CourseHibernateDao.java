package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseHibernateDao implements CourseDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Course> findByIds(long professor_id, long subject_id) {
        final TypedQuery<Course> query = em.createQuery("from Course as c " +
                "where c.professor.id = :professor and c.subject.id = :subject", Course.class);
        query.setParameter("professor", professor_id);
        query.setParameter("subject", subject_id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Course> findByProfessorId(long professor_id, int limit, int offset) {
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.professor.id = :id" +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", professor_id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filterByAreaId(long areaId, int limit, int offset) {
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.subject.area.id = :id" +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", areaId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filter(Filter filter, int limit, int offset) {
        return new LinkedList<>();
    }

    @Override
    public Course create(Professor professor, Subject subject, String description, Double price) throws CourseAlreadyExistsException {
        final Course course = new Course(professor, subject, description, price);
        em.persist(course);
        return course;
    }
}
