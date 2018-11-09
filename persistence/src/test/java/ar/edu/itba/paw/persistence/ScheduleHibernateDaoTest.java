package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
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

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class ScheduleHibernateDaoTest {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private ScheduleHibernateDao hibernateDao;

    private JdbcTemplate jdbcTemplate;

    private final String TEST_DESCRIPTION = "test description";

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }
    
    @Test
    public void testReserveValid() {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer HOUR = 4;

        Timeslot reservedTimeSlot = hibernateDao.reserveTimeSlot(mockProfessor, DAY,HOUR);
        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }

    @Test
    public void testReserveOccupied() {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(5l);
        Integer DAY = 2;
        Integer HOUR = 2;

        Timeslot reservedTimeSlot = hibernateDao.reserveTimeSlot(mockProfessor, DAY,HOUR);
        assertNull(reservedTimeSlot);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }

    @Test
    public void testGetTimeslotsForProfessor() {
        final Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(5L);
        final Integer DAY = 2;
        final Integer HOUR = 2;

        List<Timeslot> reservedTimeSlots = hibernateDao.getTimeslotsForProfessor(mockProfessor);
        assertNotNull(reservedTimeSlots);
        assertEquals(1, reservedTimeSlots.size());
        Timeslot reservedTimeSlot = reservedTimeSlots.get(0);

        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
    }


    @After
    public void tearDown(){
        cleanDatabase();
    }
}
