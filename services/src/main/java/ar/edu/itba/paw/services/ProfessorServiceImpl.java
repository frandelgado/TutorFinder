package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfessorServiceImpl implements ProfessorService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorServiceImpl.class);

    @Autowired
    private ProfessorDao professorDao;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Professor findById(final Long id) {
        LOGGER.debug("Searching for professor with id {}", id);
        return professorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Professor findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            LOGGER.error("Attempted to find professor with empty username");
            return null;
        }
        LOGGER.debug("Searching for professor with username {}", username);
        return professorDao.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public PagedResults<Professor> filterByFullName(final String fullName, final int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for professors with full name containing {}", fullName);
        final List<Professor> professors = professorDao.filterByFullName(fullName, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Professor> results;
        final int size = professors.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        if(size > PAGE_SIZE) {
            LOGGER.trace("The search has more pages to show, removing extra result");
            professors.remove(PAGE_SIZE);
            results = new PagedResults<>(professors, true);
        } else {
            LOGGER.trace("The search has no more pages to show");
            results = new PagedResults<>(professors, false);
        }
        return results;
    }

    @Override
    @Transactional
    public Professor create(final Long userId, final String description, final byte[] picture)
            throws ProfessorWithoutUserException {
        LOGGER.debug("Adding user with id {} as professor", userId);
        final User user = userDao.findById(userId).orElse(null);
        if(user == null) {
            LOGGER.error("Attempted to add a non existent user to professors");
            throw new ProfessorWithoutUserException("A valid user id must be provided in order to ");
        }

        if(description == null) {
            LOGGER.error("Attempted to create professor with null description");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempted to create professor with invalid description of size {}", description.length());
            return null;
        }
        if(picture == null) {
            LOGGER.error("Attempted to create professor without profile picture");
            return null;
        }

        final Professor professor = professorDao.create(user, description, picture);
        if(professor == null) {
            LOGGER.error("Attempted to add a non existent user to professors");
            throw new ProfessorWithoutUserException("A valid user id must be provided in order to ");
        }
        return professor;
    }

    @Transactional
    @Override
    public Professor createWithUser(final Long id, final String username, final String name,
                                    final String lastname, final String password, final String email,
                                    final String description, final byte[] picture)
            throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {

        if(username == null || password == null || email == null || name == null || lastname == null) {
            LOGGER.error("Attempted to create professor with empty fields");
            return null;
        }

        if(username.length() < 1 || username.length() > 128) {
            LOGGER.error("Attempted to create professor with invalid username length");
            return null;
        }

        if(!name.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") || !lastname.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") ||
                name.length() < 1 || lastname.length() < 1 || name.length() > 128 || lastname.length() > 128){
            LOGGER.error("Attempted to create professor with invalid name");
            return null;
        }

        if(!email.matches("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+") || email.length() < 1 || email.length() > 512){
            LOGGER.error("Attempted to create professor with invalid email");
            return null;
        }

        if(password.length() < 8 || password.length() > 64){
            LOGGER.error("Attempted to create professor with invalid password length");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempting to create professor with invalid description of size {}", description.length());
            return null;
        }

        if(picture == null) {
            LOGGER.error("Attempted to create professor without profile picture");
            return null;
        }

        final User user;
        user = userDao.create(username, password, email, name, lastname);

        LOGGER.debug("Adding user with id {} as professor", user.getId());
        return professorDao.create(user, description, picture);
    }
}
