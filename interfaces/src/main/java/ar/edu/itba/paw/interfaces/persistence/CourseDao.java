package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    Optional<Course> findByIds(final long professor_id, final long subject_id);

    List<Course> findByProfessorId(final long professor_id, final int limit, final int offset);

    List<Course> filterByAreaId(final long areaId, final int limit, final int offset);

    List<Course>  filter(final Filter filter, final int limit, final int offset);

    Course create(final Professor professor, final Subject subject, final String description, final Double price) throws CourseAlreadyExistsException;

    Comment create(final User creator, final String text, final Course course, final int rating);

    List<Comment> getComments(final Course course, final int limit, final int offset);
}
