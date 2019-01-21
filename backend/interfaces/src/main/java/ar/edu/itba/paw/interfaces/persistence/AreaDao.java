package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Area;

import java.util.List;

public interface AreaDao {
    Area findById(final long id);

    Area create(final String name, final String description, final byte[] image);

    List<Area> filterAreasByName(final String name, final int limit, final int offset);
}
