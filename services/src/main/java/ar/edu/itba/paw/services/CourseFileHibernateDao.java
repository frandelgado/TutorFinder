package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.CourseFileDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CourseFileHibernateDao implements CourseFileDao {

    @Autowired
    CourseFileDao cfd;

    @Override
    public List<CourseFile> findForCourse(Course course) {
        return cfd.findForCourse(course);
    }

    @Override
    public CourseFile findById(int id) {
        return cfd.findById(id);
    }

    @Override
    public void save(CourseFile document) {
        cfd.save(document);
    }

    @Override
    public void deleteById(int id) {
        cfd.deleteById(id);
    }
}
