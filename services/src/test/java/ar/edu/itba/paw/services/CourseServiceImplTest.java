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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CourseServiceImplTest {

    private static final int PAGE_SIZE = 3;
    private static final Long PROFESSOR_ID = 1L;
    private static final Long SUBJECT_ID = 2L;
    private static final Long INVALID_ID = 666L;
    private static final Long AREA_ID = 3L;
    private static final String DESCRIPTION = "Omne duo vim sum nudi uno quod. Latera nullam ad realem passim ii essent ut patere";
    private static final Double PRICE = 40.5;
    private static final String NAME = "Alg";

    @InjectMocks
    @Autowired
    private CourseService courseService;

    @Mock
    private CourseDao courseDao;

    @Mock
    private ProfessorService professorService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private Filter filter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCoursesByNameHasNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filter(any(), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterCoursesByNameNoNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }

        when(courseDao.filter(any(), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterCoursesByNamePageOutOfBounds() throws PageOutOfBoundsException {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);

        FilterBuilder filterBuilder = new FilterBuilder();
        when(courseDao.filter(filterBuilder.filterByName(eq(NAME)).getFilter(), anyInt(), anyInt())).thenReturn(courses);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterCoursesByNameNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, INVALID_PAGE);
    }

    @Test
    public void testCreateValid() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        final Course course = mock(Course.class);
        when(course.getProfessor()).thenReturn(professor);
        when(course.getSubject()).thenReturn(subject);
        when(courseDao.create(professor, subject, DESCRIPTION, PRICE)).thenReturn(course);

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
        assertNotNull(created);
        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
    }

    @Test
    public void testCreateInvalidPrice() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Double INVALID_PRICE = -5.0;

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, INVALID_PRICE);
        assertNull(created);
    }

    @Test
    public void testCreateInvalidDescription() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final String shortDescription = "short";
        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, shortDescription, PRICE);
        assertNull(created);
    }

    @Test(expected = NonexistentProfessorException.class)
    public void testCreateNonExistentProfessor() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {
        when(professorService.findById(PROFESSOR_ID)).thenReturn(null);

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test(expected = NonexistentSubjectException.class)
    public void testCreateNonExistentSubject() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(null);

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void testCreateCourseAlreadyExists() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        final Course course = mock(Course.class);
        when(course.getProfessor()).thenReturn(professor);
        when(course.getSubject()).thenReturn(subject);
        when(courseDao.create(professor, subject, DESCRIPTION, PRICE)).thenThrow(new CourseAlreadyExistsException());

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test
    public void testFindCoursesByProfessorIdHasNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFindCoursesByProfessorIdNoNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFindCoursesByProfessorIdPageOutOfBounds() throws PageOutOfBoundsException {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFindCoursesByProfessorIdNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, INVALID_PAGE);
    }

    @Test
    public void testFilterByAreaIdHasNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterByAreaIdNoNext() throws PageOutOfBoundsException {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterByAreaIdPageOutOfBounds() throws PageOutOfBoundsException {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterByAreaIdNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, INVALID_PAGE);
    }
}
