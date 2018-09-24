package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class CourseJdbcDaoTest {

    private static final String DESCRIPTION = "En este curso estudiaremos el algebra de forma dinamica";
    private static final Double PRICE = 45.5;
    private static final Long SUBJECT_ID = 1l;
    private static final Long PROFESSOR_ID = 2l;

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

        Professor mockProfessor = mock(Professor.class);
        Subject mockSubject = mock(Subject.class);
        when(mockProfessor.getId()).thenReturn(PROFESSOR_ID);
        when(mockSubject.getId()).thenReturn(SUBJECT_ID);
        final Course course = courseDao.create(mockProfessor, mockSubject, DESCRIPTION, PRICE);

        assertNotNull(course);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "user_id = " + PROFESSOR_ID + " AND subject_id = " + SUBJECT_ID));

    }

    @Test
    public void testFilterByTimeAndProfessor(){
        List<Course> courses = courseDao.filterCoursesByTimeAndProfessor(2,2,3, 5l);
        assertEquals(1,courses.size());
    }

    @Test
    public void testFilterByTimeNull(){
        List<Course> courses = courseDao.filterCoursesByTimeAndProfessor(2,2,3, 2l);
        assertEquals(0,courses.size());
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "courses");
    }
}
