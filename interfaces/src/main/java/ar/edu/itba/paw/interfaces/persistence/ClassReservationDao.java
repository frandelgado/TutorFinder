package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;

public interface ClassReservationDao {

    ClassReservation reserve(LocalDateTime startTime, LocalDateTime endTime, Professor professor, User student);

    ClassReservation confirm(ClassReservation classReservation, String comment);

    ClassReservation deny(ClassReservation classReservation, String comment);

}
