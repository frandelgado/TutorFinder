package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;

import java.util.List;

public interface CourseService {
    Course findCourseByIds(final long professor_id, final long subject_id);

    PagedResults<Course> findCourseByProfessorId(final long professor_id, final int page);

    PagedResults<Course> filterByAreaId(final long areaId, final int page);

    Course create(final Long professorId, final Long subjectId, final String description, final Double price)
            throws CourseAlreadyExistsException, NonexistentProfessorException, NonexistentSubjectException;

    PagedResults<Course> filterCourses(final List<Integer> days, final Integer startHour, final Integer endHour, final Double minPrice, final Double maxPrice, final String searchText, final int page);

    boolean comment(final Long userId, final Long professorId, final Long subjectId, final String body, final int rating) throws SameUserException, NonAcceptedReservationException;

    PagedResults<Comment> getComments(final Course course, final int page);

    Course modify(final Long professorId, final Long subjectId, final String description, final Double price)
            throws CourseAlreadyExistsException, NonexistentProfessorException, NonexistentSubjectException, NonexistentCourseException;

    boolean deleteCourse(long professorId, long subjectId);
}
