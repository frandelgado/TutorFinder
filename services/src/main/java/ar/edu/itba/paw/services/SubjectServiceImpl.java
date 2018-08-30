package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.SubjectDao;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {


    @Autowired
    private SubjectDao subjectDao;

    //TODO: I dont think it is a good idea to return null if there is no user, maybe an exception?
    @Override
    public Subject findSubjectById(long id) {
        return subjectDao.findById(id).orElse(null);
    }

    @Override
    public Subject create(String name, String description) {
        return subjectDao.create(name,description);
    }


}
