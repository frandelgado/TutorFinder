package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;

import java.util.List;

public interface CourseFileDao {

    List<CourseFile> findForCourse(Course course);

    CourseFile findById(int id);

    void save(CourseFile document);

    void deleteById(int id);
}

