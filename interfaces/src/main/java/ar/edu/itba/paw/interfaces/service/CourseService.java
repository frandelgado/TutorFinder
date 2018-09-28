package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.NonexistentSubjectException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;

public interface CourseService {
    Course findCourseByIds(final long professor_id, final long subject_id);

    PagedResults<Course> findCourseByProfessorId(final long professor_id, final int page) throws PageOutOfBoundsException;

    PagedResults<Course> filterCoursesByName(String name, final int page) throws PageOutOfBoundsException;

    PagedResults<Course> filterByAreaId(final long areaId, final int page) throws PageOutOfBoundsException;

    Course create(final Long professorId, final Long subjectId, final String description, final Double price)
            throws CourseAlreadyExistsException, NonexistentProfessorException, NonexistentSubjectException;

    PagedResults<Course> filterCourses(final Integer day, final Integer startHour, final Integer endHour, final Double minPrice, final Double maxPrice, final String searchText, final int page) throws PageOutOfBoundsException;
}
