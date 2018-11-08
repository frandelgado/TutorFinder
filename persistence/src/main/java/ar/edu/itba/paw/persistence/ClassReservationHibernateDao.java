package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.models.ClassRequest;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class ClassReservationHibernateDao implements ClassReservationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ClassRequest reserve(int day, int startHour, int endHour, Professor professor, User student) {
        ClassRequest classRequest = new ClassRequest(student, professor, day, startHour, endHour, 2);
        em.persist(classRequest);
        return classRequest;
    }

    @Override
    public ClassRequest confirm(ClassRequest classRequest) {
        em.merge(classRequest);
        classRequest.confirm();
        return classRequest;
    }

    @Override
    public ClassRequest deny(ClassRequest classRequest) {
        em.merge(classRequest);
        classRequest.deny();
        return classRequest;
    }
}
