package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {


    @Autowired
    private AreaDao areaDao;

    //TODO: I dont think it is a good idea to return null if there is no user, maybe an exception?
    @Override
    public Area findAreaById(long id) {
        return areaDao.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Area create(String name, String description) {
        return areaDao.create(name,description);
    }

    @Override
    public List<Area> filterAreasByName(final String name){
        return areaDao.filterAreasByName(name);
    }

}
