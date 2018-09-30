package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.NonexistentSubjectException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final int PAGE_SIZE = 3;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubjectService subjectService;

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
    public PagedResults<Course> filterCourses(final Integer day, final Integer startHour, final Integer endHour,
                                              final Double minPrice, final Double maxPrice, final String searchText,
                                              final int page) throws PageOutOfBoundsException {
        if(page <= 0){
            throw new PageOutOfBoundsException();
        }
        FilterBuilder fb = new FilterBuilder();

        if(day != null || startHour != null || endHour != null) {
            fb = fb.filterByTimeslot(day, startHour, endHour);
        }
        if(minPrice != null || maxPrice != null) {
            fb = fb.filterByPrice(minPrice, maxPrice);
        }
        if(searchText != null) {
            fb = fb.filterByName(searchText);
        }
        final List<Course> courses = courseDao.filter(fb.getFilter(), PAGE_SIZE+1, PAGE_SIZE * (page -1));
        final PagedResults<Course> results;
        final int size = courses.size();
        if(size == 0 && page > 1){
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
    public Course create(final Long professorId, final Long subjectId, final String description, final Double price)
            throws CourseAlreadyExistsException, NonexistentProfessorException, NonexistentSubjectException {

        if(price <= 0){
            return null;
        }

        if(description.length() < 50 || description.length() > 300)
            return null;

        final Professor professor = professorService.findById(professorId);
        if(professor == null) {
            throw new NonexistentProfessorException();
        }

        final Subject subject = subjectService.findSubjectById(subjectId);
        if(subject == null) {
            throw new NonexistentSubjectException();
        }

        return courseDao.create(professor, subject, description, price);
    }
}
