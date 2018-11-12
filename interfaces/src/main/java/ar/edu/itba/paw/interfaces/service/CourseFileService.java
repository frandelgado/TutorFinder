package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface CourseFileService {

    List<CourseFile> findForCourse(Course course, User user) throws UserAuthenticationException;

    CourseFile findByIdForUser(long id, User user) throws UserAuthenticationException;

    void save(CourseFile document);

    void deleteById(long id);
}
