package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private CourseDao courseDao;

    @Override
    public Course findCourseByIds(long professor_id, long subject_id) {
        return courseDao.findByIds(professor_id, subject_id).orElse(null);
    }

    @Override
    public PagedResults<Course> findCourseByProfessorId(long professor_id, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Course> courses = courseDao.findByProfessorId(professor_id, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Course> results;

        final int size = courses.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            courses.remove(PAGE_SIZE);
            results = new PagedResults<>(courses, true);
        } else {
            results = new PagedResults<>(courses, false);
        }
        return results;
    }

    @Override
    public PagedResults<Course> filterCoursesByName(final String name, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Course> courses = courseDao.filterCoursesByName(name, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Course> results;
        final int size = courses.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            courses.remove(PAGE_SIZE);
            results = new PagedResults<>(courses, true);
        } else {
            results = new PagedResults<>(courses, false);
        }
        return results;
    }

    @Override
    public PagedResults<Course> filterByAreaId(final long areaId, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Course> courses = courseDao.filterByAreaId(areaId, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Course> results;
        final int size = courses.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            courses.remove(PAGE_SIZE);
            results = new PagedResults<>(courses, true);
        } else {
            results = new PagedResults<>(courses, false);
        }
        return results;

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
