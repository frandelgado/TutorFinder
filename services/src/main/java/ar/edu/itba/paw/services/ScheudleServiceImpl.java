package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ScheudleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleDao sd;

    @Autowired
    ProfessorService ps;

    @Override
    public List<Timeslot> reserveTimeSlot(Long professor_id, Integer day, Integer startTime, Integer endTime) throws InvalidTimeException, InvalidTimeRangeException {

        List<Timeslot> reservedTimeslots = new LinkedList<>();

        if(startTime >= endTime)
            throw new InvalidTimeRangeException();
        if(startTime > 24 || startTime < 0 || endTime > 24 || endTime < 0)
            throw  new InvalidTimeException();

        Professor professor = ps.findById(professor_id);

        for(int hour = startTime; hour < endTime; hour++ ){
            reservedTimeslots.add(sd.reserveTimeSlot(professor, day, hour++));
        }

        return IntStream
                .range(startTime, endTime)
                .mapToObj(i -> sd.reserveTimeSlot(professor, day, i))
                .collect(Collectors.toList());
    }

    @Override
    public List<Timeslot> getScheduleForProfessor(Professor professor) {
        return sd.getScheduleForProfessor(professor);
    }
}
