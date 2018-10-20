package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.PagedResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    private AreaDao areaDao;

    @Override
    public Area findAreaById(long id) {
        LOGGER.debug("Searching for area with id {}", id);
        return areaDao.findById(id);
    }

    @Override
    public Area create(final String name, final String description, final byte[] image) {
        LOGGER.debug("Creating area with name {}", name);
        return areaDao.create(name,description, image);
    }


    @Override
    public PagedResults<Area> filterAreasByName(final String name, final int page){
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for areas with name containing {}", name);
        final List<Area> areas = areaDao.filterAreasByName(name, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Area> results;
        final int size = areas.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        if(size > PAGE_SIZE) {
            LOGGER.trace("The search has more pages, removing extra result");
            areas.remove(PAGE_SIZE);
            results = new PagedResults<>(areas, true);
        } else {
            LOGGER.trace("The search has no more pages to show");
            results = new PagedResults<>(areas, false);
        }
        return results;

    }

}
