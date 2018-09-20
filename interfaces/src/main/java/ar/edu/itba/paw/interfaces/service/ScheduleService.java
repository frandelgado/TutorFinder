package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;

import java.util.Set;

public interface ScheduleService {

    Set<Timeslot> createSchedule(Professor professor, Set<Timeslot> timeslots);
}
