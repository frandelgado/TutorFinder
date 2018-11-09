package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.Timeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleDao sd;

    @Autowired
    private ProfessorService ps;

    @Transactional
    @Override
    public List<Timeslot> reserveTimeSlot(final Long professor_id, final Integer day, final Integer startTime, final Integer endTime)
            throws TimeslotAllocatedException, NonexistentProfessorException {

        if(startTime >= endTime) {
            LOGGER.error("Attempted to reserve timeslot with an invalid time range");
            return null;
        }
        if(startTime > 23 || startTime < 1 || endTime > 24 || endTime < 2) {
            LOGGER.error("Attempted to reserve timeslot with invalid start time or end time");
            return null;
        }

        Professor professor = ps.findById(professor_id);

        if(professor == null) {
            LOGGER.error("Attempted to reserve timeslot for non existent professor");
            throw new NonexistentProfessorException();
        }

        List<Timeslot> list = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            LOGGER.debug("Reserving timeslot for professor with id {}, with day {}, at hour {}", professor_id,
                    day, i);
            Timeslot timeslot = sd.reserveTimeSlot(professor, day, i);

            if(timeslot == null) {
                throw new TimeslotAllocatedException();
            }

            list.add(timeslot);
        }
        return list;
    }

    @Override
    public boolean removeTimeSlot(Long professor_id, Integer day, Integer startTime, Integer endTime) throws NonexistentProfessorException {
        Professor professor = ps.findById(professor_id);
        if(professor == null)
            throw new NonexistentProfessorException();

        for (int i = startTime; i < endTime; i++) {
            LOGGER.debug("Removing timeslot for professor with id {}, with day {}, at hour {}", professor_id,
                    day, i);
            boolean removed = sd.removeTimeSlot(professor, day, i);

            if(!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Schedule getScheduleForProfessor(final Long professorId) {

        LOGGER.debug("Getting schedule for professor with id {}", professorId);
        final Professor professor = ps.findById(professorId);

        List<Integer> monday = new ArrayList<>();
        List<Integer> tuesday = new ArrayList<>();
        List<Integer> wednesday = new ArrayList<>();
        List<Integer> thursday = new ArrayList<>();
        List<Integer> friday = new ArrayList<>();
        List<Integer> saturday = new ArrayList<>();
        List<Integer> sunday = new ArrayList<>();

        List<Timeslot> timeslots = sd.getTimeslotsForProfessor(professor);
        for (Timeslot t: timeslots) {
            switch (t.getDay()){
                case 1:
                    monday.add(t.getHour());
                    break;
                case 2:
                    tuesday.add(t.getHour());
                    break;
                case 3:
                    wednesday.add(t.getHour());
                    break;
                case 4:
                    thursday.add(t.getHour());
                    break;
                case 5:
                    friday.add(t.getHour());
                    break;
                case 6:
                    saturday.add(t.getHour());
                    break;
                case 7:
                    sunday.add(t.getHour());
                    break;
            }
        }

        return new Schedule(monday,tuesday, wednesday, thursday, friday, saturday, sunday);
    }
}
