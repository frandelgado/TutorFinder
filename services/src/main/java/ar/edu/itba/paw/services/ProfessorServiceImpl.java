package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    ProfessorDao professorDao;

    @Autowired
    UserDao userService;

    @Override
    public Professor findById(final Long id) {
        return professorDao.findById(id).orElse(null);
    }

    @Override
    public Professor findByUsername(final String username) {
        return professorDao.findByUsername(username).orElse(null);
    }

    @Override
    public List<Professor> filterByFullName(String fullName) {
        return professorDao.filterByFullName(fullName);
    }

    @Override
    public Professor create(final Long userId,final String description) {
        //como el usuario tiene que existir, se chequea que exista antes de crear un profesor
        //esto puede tener problemas de concurrencia >:(
        final User user = userService.findById(userId).orElseThrow(() ->
                new ProfessorWithoutUserException("A valid user id must be provided in order to "));

        return professorDao.create(user, description);
    }

    @Override
    public Professor createWithUser(final Long id, final String username, final String name,
                                    final String lastname, final String password, final String email,
                                    final String description){
        final User user = userService.create(username, password, email, name, lastname);

        if(description.length() < 50 || description.length() > 300)
            return null;

        return professorDao.create(user, description);
    }
}
