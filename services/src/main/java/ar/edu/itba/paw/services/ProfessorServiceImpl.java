package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.ProfessorWithoutUserException;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    ProfessorDao professorDao;
    @Autowired
    UserDao userDao;
    @Override
    public Professor create(Long userId, String description) {
        //como el usuario tiene que existir, se chequea que exista antes de crear un profesor
        //esto puede tener problemas de concurrencia >:(
        User user = userDao.findById(userId).orElseThrow(() -> new ProfessorWithoutUserException("A valid user id must be provided in order to "));

        return professorDao.create(user, description);
    }
}
