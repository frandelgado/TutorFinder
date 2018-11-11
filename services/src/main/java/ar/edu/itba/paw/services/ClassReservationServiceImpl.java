package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
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
public class ClassReservationServiceImpl implements ClassReservationService {

    @Autowired
    private ClassReservationDao crd;

    @Autowired
    private UserService us;

    @Autowired
    private ProfessorService ps;

    @Override
    @Transactional
    public ClassReservation reserve(final LocalDateTime startHour, final LocalDateTime endHour, final Course course, final Long studentId) {
        final User student = us.findUserById(studentId);

        if(student == null) {
             return null;
        }
        return crd.reserve(startHour, endHour, course, student);
    }

    @Override
    @Transactional
    public ClassReservation confirm(final ClassReservation classReservation, final String comment) {
        return crd.confirm(classReservation, comment);
    }

    @Override
    @Transactional
    public ClassReservation deny(final ClassReservation classReservation, final String comment) {
        return crd.deny(classReservation, comment);
    }
}
