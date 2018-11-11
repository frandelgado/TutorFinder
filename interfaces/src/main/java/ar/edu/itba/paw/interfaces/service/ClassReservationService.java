package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.SameUserException;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

import org.joda.time.LocalDateTime;

public interface ClassReservationService {

    ClassReservation reserve(LocalDateTime startTime, LocalDateTime endTime, Course course, Long studentId) throws SameUserException;

    ClassReservation confirm(ClassReservation classReservation, String comment);

    ClassReservation deny(ClassReservation classReservation, String comment);
}

