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

    List<Course> findByProfessorId(final long professor_id);

    List<Course> filterCoursesByName(final String name);

    List<Course> pagedFilterCoursesByName(String name, int pageSize, int page);

    Integer getAmmountOfPages(int pageSize, int page);

    List<Course> filterByAreaId(final long areaId);

    List<Course> filterCoursesByTimeAndProfessor(int day, int startHour, int endHour, long professor_id);

    Course create(Professor professor, Subject subject, String description, Double price) throws CourseAlreadyExistsException;
}
