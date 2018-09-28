package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.Timeslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleDao sd;

    @Autowired
    ProfessorService ps;

    @Transactional
    @Override
    public List<Timeslot> reserveTimeSlot(Long professor_id, Integer day, Integer startTime, Integer endTime)
            throws InvalidTimeException, InvalidTimeRangeException, TimeslotAllocatedException, NonexistentProfessorException {

        if(startTime >= endTime)
            throw new InvalidTimeRangeException();
        if(startTime > 24 || startTime < 0 || endTime > 24 || endTime < 0)
            throw  new InvalidTimeException();

        Professor professor = ps.findById(professor_id);

        if(professor == null) {
            throw new NonexistentProfessorException();
        }

        List<Timeslot> list = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            Timeslot timeslot = sd.reserveTimeSlot(professor, day, i);
            list.add(timeslot);
        }
        return list;
    }

    @Override
    public Schedule getScheduleForProfessor(Professor professor) {

        List<Integer> monday = new ArrayList<>();
        List<Integer> tuesday = new ArrayList<>();;
        List<Integer> wednesday = new ArrayList<>();;
        List<Integer> thursday = new ArrayList<>();;
        List<Integer> friday = new ArrayList<>();;
        List<Integer> saturday = new ArrayList<>();;
        List<Integer> sunday = new ArrayList<>();;

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
