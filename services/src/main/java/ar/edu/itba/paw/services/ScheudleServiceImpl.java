package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScheudleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleDao sd;

    @Override
    public List<Timeslot> reserveTimeSlot(Professor professor, Integer day, Integer startTime, Integer endTime) throws InvalidTimeException, InvalidTimeRangeException {

        List<Timeslot> reservedTimeslots = new LinkedList<>();

        if(startTime >= endTime)
            throw new InvalidTimeRangeException();
        if(startTime > 24 || startTime < 0 || endTime > 24 || endTime < 0)
            throw  new InvalidTimeException();

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
