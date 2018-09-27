package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.PagedResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private AreaDao areaDao;

    @Override
    public Area findAreaById(long id) {
        return areaDao.findById(id).orElse(null);
    }

    @Override
    public Area create(String name, String description) {
        return areaDao.create(name,description);
    }


    @Override
    public PagedResults<Area> filterAreasByName(final String name, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Area> areas = areaDao.filterAreasByName(name, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Area> results;
        final int size = areas.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            areas.remove(PAGE_SIZE);
            results = new PagedResults<>(areas, true);
        } else {
            results = new PagedResults<>(areas, false);
        }
        return results;

    }

}
