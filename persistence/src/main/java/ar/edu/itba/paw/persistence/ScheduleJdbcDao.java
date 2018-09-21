package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleJdbcDao implements ScheduleDao {
    @Override
    public Timeslot reserveTimeSlot(Professor professor, Integer day, Integer hour) {
        return null;
    }
}
