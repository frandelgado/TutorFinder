package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface CourseService {
    Course findCourseByIds(final long professor_id, final long subject_id);

    List<Course> findCourseByProfessorId(final long professor_id);

    List<Course> filterCoursesByName(String name);

    Course create(Professor professor, Subject subject, String description, Double price);
}
