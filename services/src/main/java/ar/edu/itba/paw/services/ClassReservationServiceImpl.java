package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ClassReservation reserve(int day, int startHour, int endHour, Long professorId, Long studentId) {
        Professor professor = ps.findById(professorId);
        User student = us.findUserById(studentId);

        if(professor == null || student == null) {
             return null;
        }

        boolean overlap = crd.findOverlap(day, startHour, endHour, professor);

        return crd.reserve(day, startHour, endHour, professor, student);
    }

    @Override
    @Transactional
    public ClassReservation confirm(ClassReservation classReservation, String comment) {
        return crd.confirm(classReservation, comment);
    }

    @Override
    @Transactional
    public ClassReservation deny(ClassReservation classReservation, String comment) {
        return crd.deny(classReservation, comment);
    }
}
