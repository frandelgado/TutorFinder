package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

import java.util.List;
import java.util.Set;

public interface ScheduleService {

    List<Timeslot> reserveTimeSlot(Long professor_id, Integer day, Integer startTime, Integer endTime) throws InvalidTimeException, InvalidTimeRangeException;

    List<Timeslot> getScheduleForProfessor(Professor professor);
}
