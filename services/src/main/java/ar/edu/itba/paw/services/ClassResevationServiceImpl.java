package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.interfaces.service.ClassResevationService;
import ar.edu.itba.paw.models.ClassRequest;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassResevationServiceImpl implements ClassResevationService {

    @Autowired
    private ClassReservationDao crd;

    @Override
    @Transactional
    public ClassRequest reserve(int day, int startHour, int endHour, Professor professor, User student) {
        return crd.reserve(day, startHour, endHour, professor, student);
    }

    @Override
    @Transactional
    public ClassRequest confirm(ClassRequest classRequest) {
        return crd.confirm(classRequest);
    }

    @Override
    @Transactional
    public ClassRequest deny(ClassRequest classRequest) {
        return crd.deny(classRequest);
    }
}
