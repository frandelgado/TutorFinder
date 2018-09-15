package ar.edu.itba.paw.services;

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

    private final static Double INITIAL_RATING = 3.0;

    @Autowired
    private CourseDao courseDao;

    //TODO: I dont think it is a good idea to return null if there is no user, maybe an exception?
    @Override
    public Course findCourseByIds(long professor_id, long subject_id) {
        return courseDao.findByIds(professor_id, subject_id).orElse(null);
    }

    @Override
    public List<Course> filterCoursesByName(final String name){
        return courseDao.filterCoursesByName(name);
    }

    @Override
    public Course create(Professor professor, Subject subject, String description, Double price) {
        return courseDao.create(professor, subject, description, price, INITIAL_RATING);
    }

}
