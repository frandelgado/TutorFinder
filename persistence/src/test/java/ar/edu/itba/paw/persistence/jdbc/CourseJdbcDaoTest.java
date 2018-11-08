package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.CourseJdbcDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcTestConfig.class)
@Sql("classpath:schema.sql")
public class CourseJdbcDaoTest {

    private static final String DESCRIPTION = "Curso de algebra";
    private static final Double PRICE = 240.0;
    private static final Long SUBJECT_ID = 1L;
    private static final Long PROFESSOR_ID = 5L;
    private static final Long AREA_ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_NAME = "InvalidTestName";
    private static final String SUBJECT_NAME = "Alge";
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;
    private static final Integer DAY = 2;
    private static final Integer STARTHOUR = 2;
    private static final Integer ENDHOUR = 3;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CourseJdbcDao courseDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testCreateValid() throws CourseAlreadyExistsException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "courses");
        Professor mockProfessor = mock(Professor.class);
        Subject mockSubject = mock(Subject.class);
        when(mockProfessor.getId()).thenReturn(PROFESSOR_ID);
        when(mockSubject.getId()).thenReturn(SUBJECT_ID);
        final Course course = courseDao.create(mockProfessor, mockSubject, DESCRIPTION, PRICE);

        assertNotNull(course);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "user_id = " + PROFESSOR_ID + " AND subject_id = " + SUBJECT_ID));

    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void testCreateInvalid() throws CourseAlreadyExistsException {
        Professor mockProfessor = mock(Professor.class);
        Subject mockSubject = mock(Subject.class);
        when(mockProfessor.getId()).thenReturn(PROFESSOR_ID);
        when(mockSubject.getId()).thenReturn(SUBJECT_ID);
        final Course course = courseDao.create(mockProfessor, mockSubject, DESCRIPTION, PRICE);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "user_id = " + PROFESSOR_ID + " AND subject_id = " + SUBJECT_ID));

    }

    @Test
    public void testFindByIdsValid() {
        final Course course = courseDao.findByIds(PROFESSOR_ID, SUBJECT_ID).orElse(null);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }

    @Test
    public void testFindByIdsInvalid() {
        final Course course = courseDao.findByIds(INVALID_ID, INVALID_ID).orElse(null);
        assertNull(course);
    }

    @Test
    public void testFindByProfessorIdValid() {
        final List<Course> courses = courseDao.findByProfessorId(PROFESSOR_ID, LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(1, courses.size());

        final Course course = courses.get(0);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }

    @Test
    public void testFindByProfessorIdInvalid() {
        final List<Course> courses = courseDao.findByProfessorId(INVALID_ID, LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @Test
    public void testFilterCoursesByNameValid() {
        FilterBuilder filterBuilder = new FilterBuilder();
        final List<Course> courses = courseDao.filter(filterBuilder.filterByName(SUBJECT_NAME).getFilter(), LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(1, courses.size());

        final Course course = courses.get(0);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }

    @Test
    public void testFilterCoursesByNameInvalid() {
        FilterBuilder filterBuilder = new FilterBuilder();
        final List<Course> courses = courseDao.filter(filterBuilder.filterByName(INVALID_NAME).getFilter(), LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @Test
    public void testFilterByTimeslotValid(){
        FilterBuilder filterBuilder = new FilterBuilder();
        final List<Course> courses = courseDao.filter(filterBuilder.filterByTimeslot(DAY, STARTHOUR, ENDHOUR).getFilter(), LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(1, courses.size());

        final Course course = courses.get(0);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }


    @Test
    public void testFilerByPriceValid(){
        FilterBuilder filterBuilder = new FilterBuilder();
        final List<Course> courses = courseDao.filter(filterBuilder.filterByPrice(PRICE,PRICE).getFilter(), LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(1, courses.size());

        final Course course = courses.get(0);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }

    @Test
    public void testFilterByAreaIdValid() {
        final List<Course> courses = courseDao.filterByAreaId(AREA_ID, LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(1, courses.size());

        final Course course = courses.get(0);
        assertNotNull(course);

        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
        assertEquals(DESCRIPTION, course.getDescription());
        assertEquals(PRICE, course.getPrice());
    }

    @Test
    public void testFilterByAreaIdInvalid() {
        final List<Course> courses = courseDao.filterByAreaId(INVALID_ID, LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }
}
