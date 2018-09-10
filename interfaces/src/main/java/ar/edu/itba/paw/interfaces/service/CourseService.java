package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Course;

import java.util.List;

public interface CourseService {
    Course findCourseByIds(final long professor_id, final long subject_id);

    List<Course> filterCoursesByName(String name);
}
