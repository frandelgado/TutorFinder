package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

import java.util.Set;

public class ScheudleServiceImpl implements ScheduleService {


    @Override
    public Set<Timeslot> reserveTimeSlot(Professor professor, Integer day, Integer startTime, Integer EndTime) {
        return null;
    }
}
