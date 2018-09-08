package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Area;

import java.util.List;
import java.util.Optional;

public interface AreaDao {
    Optional<Area> findById(final long id);

    Area create(final String name, final String description);

    List<Area> filterAreasByName(final String name);
}
