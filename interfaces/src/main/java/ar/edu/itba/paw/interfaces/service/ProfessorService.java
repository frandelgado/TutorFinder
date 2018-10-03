package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;

public interface ProfessorService {

    Professor findById(final Long id);

    Professor findByUsername(final String username);

    PagedResults<Professor> filterByFullName(final String fullName, final int page);

    Professor create(final Long userId, final String description, final byte[] picture) throws ProfessorWithoutUserException;

    Professor createWithUser(final Long id, final String username, final String name, final String lastname,
                             final String password, final String email, final String description, final byte[] picture)
            throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException;
}
