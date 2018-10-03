package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.Timeslot;

import java.util.List;

public interface ScheduleService {

    List<Timeslot> reserveTimeSlot(Long professor_id, Integer day, Integer startTime, Integer endTime) throws TimeslotAllocatedException, NonexistentProfessorException;

    Schedule getScheduleForProfessor(Long professorId);
}
