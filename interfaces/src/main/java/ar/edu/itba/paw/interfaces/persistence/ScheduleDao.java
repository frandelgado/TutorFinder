package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

public interface ScheduleDao {
    Timeslot reserveTimeSlot(Professor professor, Integer day, Integer hour);
}
