package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CourseFileDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class CourseFileHibernateDao implements CourseFileDao {

    @Autowired
    EntityManager em;

    @Override
    public List<CourseFile> findForCourse(Course course) {
        em.merge(course);
        TypedQuery<CourseFile> query = em.createQuery("from CourseFile as c where c.course :=course", CourseFile.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

    @Override
    public CourseFile findById(long id) {
        return em.find(CourseFile.class, id);
    }

    @Override
    public void save(CourseFile document) {
        //TODO: se necesita el merge?
        em.merge(document);
        em.persist(document);
    }

    @Override
    public void deleteById(long id) {
        CourseFile courseFile = em.find(CourseFile.class, id);
        if(courseFile != null) {
            em.remove(courseFile);
        }
    }
}
