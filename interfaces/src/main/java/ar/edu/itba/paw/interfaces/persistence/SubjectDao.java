package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.Optional;

public interface SubjectDao {
    Optional<Subject> findById(final long id);
}
