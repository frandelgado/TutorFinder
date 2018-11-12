package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.SameUserException;
import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

import org.joda.time.LocalDateTime;

public interface ClassReservationService {

    ClassReservation reserve(final LocalDateTime startTime, final LocalDateTime endTime,
                             final Course course, final Long studentId) throws SameUserException;

    ClassReservation confirm(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException;


    boolean hasAcceptedReservation(User student, Course course);

    ClassReservation deny(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException;

    ClassReservation findById(final Long classReservationId);
}

