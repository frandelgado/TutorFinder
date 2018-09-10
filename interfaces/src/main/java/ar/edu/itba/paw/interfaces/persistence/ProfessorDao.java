package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

public interface ProfessorDao {

    Professor create(final User user, final String description);

}
