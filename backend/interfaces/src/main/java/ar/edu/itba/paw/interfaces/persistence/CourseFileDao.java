package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;

import java.util.List;

public interface CourseFileDao {

    List<CourseFile> findForCourse(Course course);

    CourseFile findById(long id);

    CourseFile save(final Course course, final String fileName, final String description, final String contentType, final byte[] file);

    void deleteById(long id);
}

