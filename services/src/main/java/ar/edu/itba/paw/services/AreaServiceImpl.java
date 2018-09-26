package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
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
    public List<Area> filterAreasByName(final String name){
        return areaDao.filterAreasByName(name);
    }

    @Override
    public List<Area> filterAreasByName(final String name, final int page) throws PageOutOfBoundsException {
        if(page < 0) {
            throw new PageOutOfBoundsException();
        }

        return areaDao.filterAreasByName(name, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

}
