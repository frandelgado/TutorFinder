package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.InvalidDateException;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class SceduleJdbcDaoTest {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private ScheduleJdbcDao scheduleJdbcDao;

    private JdbcTemplate jdbcTemplate;

    private final String TEST_DESCRIPTION = "test description";

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"schedules");
    }
    
    @Test
    public void testReserveValid(){
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer HOUR = 4;

        Timeslot reservedTimeSlot = scheduleJdbcDao.reserveTimeSlot(mockProfessor, DAY,HOUR);
        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }

    @Test
    public void testReserveOccupied(){
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer HOUR = 4;

        Timeslot reservedTimeSlot = scheduleJdbcDao.reserveTimeSlot(mockProfessor, DAY,HOUR);
        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }
}
