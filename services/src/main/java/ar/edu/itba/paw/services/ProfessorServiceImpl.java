package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private static final int PAGE_SIZE = 3;

    @Autowired
    ProfessorDao professorDao;

    @Autowired
    UserDao userDao;

    @Override
    public Professor findById(final Long id) {
        return professorDao.findById(id).orElse(null);
    }

    @Override
    public Professor findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return professorDao.findByUsername(username).orElse(null);
    }

    @Override
    public PagedResults<Professor> filterByFullName(final String fullName, final int page)
            throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Professor> professors = professorDao.filterByFullName(fullName, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Professor> results;
        final int size = professors.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            professors.remove(PAGE_SIZE);
            results = new PagedResults<>(professors, true);
        } else {
            results = new PagedResults<>(professors, false);
        }
        return results;
    }

    @Override
    public Professor create(final Long userId, final String description) throws ProfessorWithoutUserException {
        final User user = userDao.findById(userId).orElseThrow(() ->
                new ProfessorWithoutUserException("A valid user id must be provided in order to "));
        return professorDao.create(user, description);
    }

    @Transactional
    @Override
    public Professor createWithUser(final Long id, final String username, final String name,
                                    final String lastname, final String password, final String email,
                                    final String description) throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final User user;
        user = userDao.create(username, password, email, name, lastname);

        if(description.length() < 50 || description.length() > 300)
            return null;

        return professorDao.create(user, description);
    }
}
