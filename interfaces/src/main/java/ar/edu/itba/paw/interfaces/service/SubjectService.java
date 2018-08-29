package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Subject;

public interface SubjectService {
    Subject findSubjectById(final long id);

    Subject create(final String name, final String description);

}
