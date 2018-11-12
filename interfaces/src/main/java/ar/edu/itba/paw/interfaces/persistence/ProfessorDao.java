package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface ProfessorDao {

    Professor create(final User user, final String description, final byte[] picture);

    Professor modify(Professor professor, final String description, final byte[] picture);

    Optional<Professor> findById(final Long professor_id);

    Optional<Professor> findByUsername(final String username);

    List<Professor> filterByFullName(final String fullName, final int limit, final int offset);

    Professor merge(Professor professor);

    List<ClassReservation> getPagedClassRequests(final Long professorId, final int limit, final int offset);
}
