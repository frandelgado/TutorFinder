package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Professor;

public interface ScheduleDao {
    void reserveTimeSlot(Professor professor, Integer day, Integer hour);
}
