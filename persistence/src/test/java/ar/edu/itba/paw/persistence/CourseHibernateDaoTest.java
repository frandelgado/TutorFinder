package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.*;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class CourseHibernateDaoTest {

    private static final String DESCRIPTION = "Curso de algebra";
    private static final Double PRICE = 240.0;
    private static final String NEW_DESCRIPTION = "No Curso de algebra";
    private static final Double NEW_PRICE = 340.0;
    private static final Long SUBJECT_ID = 1L;
    private static final Long USER_ID = 3L;
    private static final Long PROFESSOR_ID = 5L;
    private static final Long AREA_ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_NAME = "InvalidTestName";
    private static final String EMPTY_STRING = "";
    private static final String SUBJECT_NAME = "Alge";
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;
    private static final Integer DAY = 2;
    private static final Integer STARTHOUR = 2;
    private static final Integer ENDHOUR = 3;
    private static final int RATING = 4;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CourseHibernateDao courseDao;

    private JdbcTemplate jdbcTemplate;

    private Professor professorTest;

    private Subject subjectTest;

    private Course courseTest;

    private User userTest;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        subjectTest = em.find(Subject.class, SUBJECT_ID);
        professorTest = em.find(Professor.class, PROFESSOR_ID);
        userTest = em.find(User.class, USER_ID);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreateValid() throws CourseAlreadyExistsException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "courses");
        final Course course = courseDao.create(professorTest, subjectTest, DESCRIPTION, PRICE);

        em.flush();

        assertNotNull(course);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "user_id = " + PROFESSOR_ID + " AND subject_id = " + SUBJECT_ID));

    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void testCreateInvalid() throws CourseAlreadyExistsException {
        final Course course = courseDao.create(professorTest, subjectTest, DESCRIPTION, PRICE);

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
        final List<Course> courses = courseDao.filter(null, null, null, null, null, SUBJECT_NAME, LIMIT, OFFSET);
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
        final List<Course> courses = courseDao.filter(null, null, null, null, null, INVALID_NAME, LIMIT, OFFSET);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @Test
    public void testFilterByTimeslotValid(){
        final List<Integer> days = new ArrayList<>();
        days.add(DAY);
        final List<Course> courses = courseDao.filter(days, STARTHOUR, ENDHOUR, null, null, EMPTY_STRING, LIMIT, OFFSET);
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
        final List<Course> courses = courseDao.filter(null, null, null, PRICE, PRICE, EMPTY_STRING, LIMIT, OFFSET);
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

    @Test
    public void testDeleteValid() {

        courseTest = new Course(professorTest, subjectTest, DESCRIPTION, PRICE);

        final boolean deleted = courseDao.delete(courseTest);
        em.flush();

        assertTrue(deleted);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "user_id = " + PROFESSOR_ID + " AND subject_id = " + SUBJECT_ID));

    }

    @Test
    public void testModifyValid() {

        courseTest = new Course(professorTest, subjectTest, DESCRIPTION, PRICE);
        courseTest.setComments(new LinkedList<>());

        final Course modified = courseDao.modify(courseTest, NEW_DESCRIPTION, NEW_PRICE);
        em.flush();

        assertNotNull(modified);
        assertEquals(NEW_DESCRIPTION, modified.getDescription());
        assertEquals(NEW_PRICE, modified.getPrice());
    }

    @Test
    public void testCommentValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "comments");
        courseTest = new Course(professorTest, subjectTest, DESCRIPTION, PRICE);

        final Comment comment = courseDao.create(userTest, DESCRIPTION, courseTest, RATING);
        em.flush();

        assertNotNull(comment);
        assertEquals(DESCRIPTION, comment.getComment());
        assertEquals(RATING, comment.getRating().intValue());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "comments"));

    }

    @After
    public void tearDown(){
        cleanDatabase();
    }
}
