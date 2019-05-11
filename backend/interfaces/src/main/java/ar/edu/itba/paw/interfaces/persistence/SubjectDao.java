package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDao {
    Optional<Subject> findById(final long id);

    Subject create(final String name, final String description, final Long area_id);

    List<Subject> filterSubjectsByName(final String name);

    List<Subject> getAvailableSubjects(final long id);
}
