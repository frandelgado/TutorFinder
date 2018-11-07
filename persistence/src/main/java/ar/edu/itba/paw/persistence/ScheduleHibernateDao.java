package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ScheduleHibernateDao implements ScheduleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Timeslot reserveTimeSlot(Professor professor, Integer day, Integer hour) {
        Timeslot timeslot = new Timeslot(day, hour, professor);
        em.persist(timeslot);
        return timeslot;
    }

    @Override
    public List<Timeslot> getTimeslotsForProfessor(Professor professor) {
        em.merge(professor);
        return professor.getTimeslots();
    }
}
