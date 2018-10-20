package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Filter;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public class CourseHibernateDao implements CourseDao {

    @Override
    public Optional<Course> findByIds(long professor_id, long subject_id) {
        return Optional.empty();
    }

    @Override
    public List<Course> findByProfessorId(long professor_id, int limit, int offset) {
        return null;
    }

    @Override
    public List<Course> filterByAreaId(long areaId, int limit, int offset) {
        return null;
    }

    @Override
    public List<Course> filter(Filter filter, int limit, int offset) {
        return null;
    }

    @Override
    public Course create(Professor professor, Subject subject, String description, Double price) throws CourseAlreadyExistsException {
        return null;
    }
}
