package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ScheduleServiceImplTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void testValidRange() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 16;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
        assertEquals(2, timeslots.size());
    }

    @Test
    public void testUnitaryRange() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 15;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
        assertEquals(1, timeslots.size());
        Timeslot timeslot = timeslots.iterator().next();

        //The timeslot hour is the start hour and timeslots are all one hr long
        assertEquals(START_HOUR, timeslot.getHour());
        assertEquals(DAY, timeslot.getDay());
    }

    @Test(expected = InvalidTimeRangeException.class)
    public void testInvalidRange() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
    }

    @Test(expected = InvalidTimeException.class)
    public void testInvalidHourUpper() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer START_HOUR = -3;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
    }

    @Test(expected = InvalidTimeException.class)
    public void testInvalidHourLower() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 27;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
    }


    @Test(expected = InvalidTimeException.class)
    public void testInvalidDayUpper() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = 8;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);
    }

    @Test(expected = InvalidTimeException.class)
    public void testInvalidDayLower() throws InvalidTimeRangeException, InvalidTimeException {
        Professor mockProfessor = mock(Professor.class);
        when(mockProfessor.getId()).thenReturn(2l);
        Integer DAY = -4;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;
        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(mockProfessor, DAY, START_HOUR, END_HOUR);

    }
}
