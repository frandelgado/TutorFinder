package ar.edu.itba.paw.persistence;

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

import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class ClasReservationHibernateDaoTest {

    private static final Long ACCEPTED_RESERVATION_USER_ID      = 1L;
    private static final Long NO_RESERVATION_USER_ID            = 5L;
    private static final Long DENIED_RESERVATION_USER_ID        = 3L;
    private static final Long UNSPECIFIED_RESERVATION_USER_ID   = 4L;
    private static final Long PROFESSOR_ID                      = 2L;
    private static final Long SUBJECT_ID                        = 1L;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClassReservationHibernateDao crd;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void hasAcceptedReservationTest() {
        User student = mock(User.class);
        Course course  = mock(Course.class);
        when(student.getId()).thenReturn(ACCEPTED_RESERVATION_USER_ID);
        assertTrue(crd.hasAcceptedReservation(student, course));
    }

    @Test
    public void hasDeniedReservationTest() {
        User student = mock(User.class);
        Course course  = mock(Course.class);
        when(student.getId()).thenReturn(DENIED_RESERVATION_USER_ID);
        assertFalse(crd.hasAcceptedReservation(student, course));
    }

    @Test
    public void hasUnspecifiedReservationTest() {
        User student = mock(User.class);
        Course course  = mock(Course.class);
        when(student.getId()).thenReturn(UNSPECIFIED_RESERVATION_USER_ID);
        assertFalse(crd.hasAcceptedReservation(student, course));
    }

    @Test
    public void noReservationTest() {
        User student = mock(User.class);
        Course course  = mock(Course.class);
        when(student.getId()).thenReturn(NO_RESERVATION_USER_ID);
        assertFalse(crd.hasAcceptedReservation(student, course));
    }

    @After
    public void tearDown(){
        cleanDatabase();
    }
}
