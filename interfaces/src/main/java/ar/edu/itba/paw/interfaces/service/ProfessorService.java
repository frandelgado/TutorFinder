package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Professor;

public interface ProfessorService {

    Professor create(final Long userId, final String description);
}