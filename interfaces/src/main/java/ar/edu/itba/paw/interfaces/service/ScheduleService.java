package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

import java.util.Set;

public interface ScheduleService {

    Set<Timeslot> reserveTimeSlot(Professor professor, Integer day, Integer startTime, Integer EndTime);
}
