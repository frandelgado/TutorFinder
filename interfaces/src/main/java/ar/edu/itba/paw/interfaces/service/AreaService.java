package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Area;

import java.util.List;

public interface AreaService {
    Area findAreaById(final long id);

    Area create(final String name, final String description);

    List<Area> filterAreasByName(String name);
}
