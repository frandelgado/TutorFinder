package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.SameUserException;
import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.joda.time.LocalDateTime;

@Service
@Transactional
public class ClassReservationServiceImpl implements ClassReservationService {

    @Autowired
    private ClassReservationDao crd;

    @Autowired
    private UserService us;

    @Autowired
    private ProfessorService ps;

    @Override
    @Transactional
    public ClassReservation reserve(final LocalDateTime startHour, final LocalDateTime endHour, final Course course, final Long studentId) throws SameUserException {
        final User student = us.findUserById(studentId);

        if(course.getProfessor().getId().equals(studentId)){
            throw new SameUserException();
        }

        if(student == null) {
             return null;
        }
        return crd.reserve(startHour, endHour, course, student);
    }

    @Override
    @Transactional
    public ClassReservation confirm(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException {
        ClassReservation classReservation = findById(classReservationId);
        if(classReservation == null) {
            return null;
        }

        if(!classReservation.getCourse().getProfessor().getId().equals(professorId)) {
            throw new UserAuthenticationException();
        }
        return crd.confirm(classReservation, comment);
    }

    @Override
    @Transactional
    public ClassReservation deny(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException {
        ClassReservation classReservation = findById(classReservationId);
        if(classReservation == null) {
            return null;
        }

        if(!classReservation.getCourse().getProfessor().getId().equals(professorId)) {
            throw new UserAuthenticationException();
        }
        return crd.deny(classReservation, comment);
    }

    @Override
    public boolean hasAcceptedReservation(final User student, final Course course) {
        return crd.hasAcceptedReservation(student, course);
    }

    @Override
    public ClassReservation findById(Long classReservationId) {
        return crd.findById(classReservationId);
    }

}
