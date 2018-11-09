package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.joda.time.LocalDateTime;

@Repository
public class ClassReservationHibernateDao implements ClassReservationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ClassReservation reserve(LocalDateTime startHour, LocalDateTime endHour, Professor professor, User student) {
        ClassReservation classReservation = new ClassReservation(student, professor, startHour, endHour, 2);
        em.persist(classReservation);
        return classReservation;
    }

    @Override
    public ClassReservation confirm(ClassReservation classReservation, String comment) {
        em.merge(classReservation);
        classReservation.confirm(comment);
        return classReservation;
    }

    @Override
    public ClassReservation deny(ClassReservation classReservation, String comment) {
        em.merge(classReservation);
        classReservation.deny(comment);
        return classReservation;
    }
}
