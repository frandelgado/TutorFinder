package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.models.Professor;

import java.util.List;

public interface ProfessorService {

    Professor findById(final Long id);

    Professor findByUsername(final String username);

    List<Professor> filterByFullName(final String fullName);

    List<Professor> filterByFullName(final String fullName, final int page) throws PageOutOfBoundsException;

    Professor create(final Long userId, final String description) throws ProfessorWithoutUserException;

    Professor createWithUser(final Long id, final String username, final String name, final String lastname,
                             final String password, final String email, final String description) throws EmailAlreadyInUseException, UsernameAlreadyInUseException;
}
