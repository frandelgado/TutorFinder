package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Override
    public Course findCourseByIds(long professor_id, long subject_id) {
        return courseDao.findByIds(professor_id, subject_id).orElse(null);
    }

    @Override
    public List<Course> findCourseByProfessorId(long professor_id) {
        return courseDao.findByProfessorId(professor_id);
    }

    @Override
    public List<Course> filterCoursesByName(final String name){
        return courseDao.filterCoursesByName(name);
    }

    @Override
    public List<Course> filterByAreaId(final long areaId) {
        return courseDao.filterByAreaId(areaId);
    }

    @Override
    public Course create(Professor professor, Subject subject, String description, Double price)
            throws CourseAlreadyExistsException {

        if(price <= 0){
            return null;
        }

        if(description.length() < 50 || description.length() > 300)
            return null;

        return courseDao.create(professor, subject, description, price);
    }


}
