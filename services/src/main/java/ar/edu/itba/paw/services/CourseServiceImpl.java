package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ClassReservationService classReservationService;

    @Override
    public Course findCourseByIds(final long professor_id, final long subject_id) {
        LOGGER.debug("Searching for course taught by professor with id {} about subject with id {}",
                professor_id, subject_id);
        return courseDao.findByIds(professor_id, subject_id).orElse(null);
    }

    @Override
    public PagedResults<Course> findCourseByProfessorId(final long professor_id, final int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for courses taught by professor with id {}", professor_id);
        final List<Course> courses = courseDao.findByProfessorId(professor_id, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Course> results;

        final int size = courses.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        if(size > PAGE_SIZE) {
            courses.remove(PAGE_SIZE);
            LOGGER.trace("The search has more pages, removing extra result");
            results = new PagedResults<>(courses, true);
        } else {
            results = new PagedResults<>(courses, false);
            LOGGER.trace("The search has no more pages to show");
        }
        return results;
    }

    @Override
    public PagedResults<Course> filterByAreaId(final long areaId, final int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for courses from area with id {}", areaId);
        final List<Course> courses = courseDao.filterByAreaId(areaId, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Course> results;
        final int size = courses.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        if(size > PAGE_SIZE) {
            LOGGER.trace("The search has more results to show, removing extra result");
            courses.remove(PAGE_SIZE);
            results = new PagedResults<>(courses, true);
        } else {
            LOGGER.trace("The search has no more pages to show");
            results = new PagedResults<>(courses, false);
        }
        return results;

    }

    @Override
    public PagedResults<Course> filterCourses(final List<Integer> days, final Integer startHour, final Integer endHour,
                                              final Double minPrice, final Double maxPrice, final String searchText,
                                              final int page) {
        if(page <= 0){
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }
        LOGGER.debug("Creating filter builder");
        FilterBuilder fb = new FilterBuilder();

        if((days != null && !days.isEmpty()) || startHour != null || endHour != null) {
            LOGGER.debug("Adding filter by timeslot");
            if(days != null) {
                for (Integer day : days) {
                    fb = fb.filterByTimeslot(day, startHour, endHour);
                }
            } else {
                fb = fb.filterByTimeslot(null, startHour, endHour);
            }
        }
        if(minPrice != null || maxPrice != null) {
            LOGGER.debug("Adding filter with price");
            fb = fb.filterByPrice(minPrice, maxPrice);
        }
        if(searchText != null) {
            LOGGER.debug("Adding filter by search text containing {}", searchText);
            fb = fb.filterByName(searchText);
        }
        final List<Course> courses = courseDao.filter(fb.getFilter(), PAGE_SIZE+1, PAGE_SIZE * (page -1));
        final PagedResults<Course> results;
        final int size = courses.size();
        if(size == 0 && page > 1){
            LOGGER.error("Page number exceeds total page count");
            return null;
        }
        if(size > PAGE_SIZE) {
            LOGGER.trace("Search has more results to show, removing extra result");
            courses.remove(PAGE_SIZE);
            results = new PagedResults<>(courses, true);
        } else {
            LOGGER.trace("Search has no more pages to show");
            results = new PagedResults<>(courses, false);
        }

        return results;
    }

    @Override
    public Course create(final Long professorId, final Long subjectId, final String description, final Double price)
            throws CourseAlreadyExistsException, NonexistentProfessorException, NonexistentSubjectException {

        if(price < 1){
            LOGGER.error("Attempted to create course with price lower than 1");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempted to create course with invalid description of size {}", description.length());
            return null;
        }

        final Professor professor = professorService.findById(professorId);
        if(professor == null) {
            LOGGER.error("Attempted to create course for non existent professor");
            throw new NonexistentProfessorException();
        }

        final Subject subject = subjectService.findSubjectById(subjectId);
        if(subject == null) {
            LOGGER.error("Attempted to create course with a non existent subject");
            throw new NonexistentSubjectException();
        }

        LOGGER.debug("Creating course taught by professor with id {} about subject with id {}", professorId, subjectId);

        final boolean exists = courseDao.findByIds(professorId, subjectId).isPresent();

        if(exists) {
            LOGGER.error("Course with user_id {} and subject_id {} already exists", professor.getId(), subject.getId());
            throw new CourseAlreadyExistsException();
        }

        return courseDao.create(professor, subject, description, price);
    }

    @Transactional
    @Override
    public boolean comment(final Long userId, final Long professorId, final Long subjectId, final String body,
                           final int rating) throws SameUserException, NonAcceptedReservationException {

        final User user = userService.findUserById(userId);
        final Course course = findCourseByIds(professorId, subjectId);

        if(user == null || course == null) {
            LOGGER.error("Attempted to post comment with invalid parameters");
            return false;
        }

        if(rating > 5 || rating < 1) {
            LOGGER.error("Attempted to post comment invalid rating");
            return false;
        }

        if(body == null || body.length() < 1 || body.length() > 1024) {
            LOGGER.error("Attempted to post comment invalid body size");
            return false;
        }

        if(user.getId().equals(professorId)) {
            LOGGER.error("User attempted to post comment in his own course");
            throw new SameUserException();
        }

        if(!classReservationService.hasAcceptedReservation(user, course)) {
            LOGGER.error("User attempted to post comment in a course to which he has not attended to");
            throw new NonAcceptedReservationException();
        }
        LOGGER.debug("Posting comment from user with id {} in course of subject with id {} and professor with id {}",
                userId, subjectId, professorId);
        final Comment comment = courseDao.create(user, body, course, rating);

        return comment != null;
    }

    @Override
    public PagedResults<Comment> getComments(final Course course, final int page) {

        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for comments in course taught by professor with id {} about subject with id {}",
                course.getProfessor().getId(), course.getSubject().getId());
        final List<Comment> comments = courseDao.getComments(course, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Comment> results;
        final int size = comments.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        if(size > PAGE_SIZE) {
            comments.remove(PAGE_SIZE);
            results = new PagedResults<>(comments, true);
            LOGGER.trace("The search has more pages, removing extra result");
        } else {
            LOGGER.trace("The search has no more pages to show");
            results = new PagedResults<>(comments, false);
        }
        return results;
    }

    @Override
    public Course modify(final Long professorId, final Long subjectId, final String description, final Double price)
            throws NonexistentCourseException {

        if(price < 1){
            LOGGER.error("Attempted to modify course with price lower than 1");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempted to modify course with invalid description of size {}", description.length());
            return null;
        }

        final Optional<Course> course = courseDao.findByIds(professorId, subjectId);
        if(!course.isPresent()) {
            LOGGER.error("Attempted to modify non existent course");
            throw new NonexistentCourseException();
        }

        LOGGER.debug("Modifying course taught by professor with id {} about subject with id {}", professorId, subjectId);

        return courseDao.modify(course.get(), description, price);
    }


    @Override
    public boolean deleteCourse(final long professorId, final long subjectId) {
        final Optional<Course> course = courseDao.findByIds(professorId, subjectId);
        LOGGER.debug("Deleting course taught by professor with id {} about subject with id {}", professorId, subjectId);

        if(!course.isPresent()) {
            LOGGER.error("Attempted to delete non existent course");
            return false;
        }

        return courseDao.delete(course.get());
    }
}
