package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    Optional<Course> findByIds(final long professor_id, final long subject_id);

    List<Course> findByProfessorId(final long professor_id);

    List<Course> filterCoursesByName(final String name);

    Course create(Professor professor, Subject subject, String description, Double price);
}
