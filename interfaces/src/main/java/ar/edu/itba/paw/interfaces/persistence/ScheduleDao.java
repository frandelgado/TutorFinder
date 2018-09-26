package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

import java.util.List;

public interface ScheduleDao {
    Timeslot reserveTimeSlot(Professor professor, Integer day, Integer hour) throws TimeslotAllocatedException;

    List<Timeslot> getScheduleForProfessor(Professor professor);
}
