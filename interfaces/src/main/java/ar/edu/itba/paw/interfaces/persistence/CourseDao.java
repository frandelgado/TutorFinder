package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.Timeslot;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    Optional<Course> findByIds(final long professor_id, final long subject_id);

    List<Course> findByProfessorId(final long professor_id, final int limit, final int offset);

    List<Course> filterCoursesByName(final String name, final int limit, final int offset);

    List<Course> filterByAreaId(final long areaId, final int limit, final int offset);

    List<Course> filterCoursesByTimeAndProfessor(int day, int startHour, int endHour, long professor_id);

    Course create(Professor professor, Subject subject, String description, Double price) throws CourseAlreadyExistsException;
}
