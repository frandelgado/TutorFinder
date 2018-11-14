package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CourseFileDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class CourseFileHibernateDao implements CourseFileDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseFileHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CourseFile> findForCourse(final Course course) {
        LOGGER.trace("Finding files for course taught by professor with id {} and subject {}", course.getProfessor().getId(),
                course.getSubject().getId());
        em.merge(course);
        TypedQuery<CourseFile> query = em.createQuery("from CourseFile as c where c.course = :course", CourseFile.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

    @Override
    public CourseFile findById(final long id) {
        LOGGER.trace("Finding file with id {}", id);
        return em.find(CourseFile.class, id);
    }

    @Override
    public CourseFile save(final Course course, final String fileName, final String description, final String contentType, final byte[] file) {

        LOGGER.trace("Saving file for course taught by professor with id {} and subject {}", course.getProfessor().getId(),
                course.getSubject().getId());
        final Course merged = em.merge(course);
        CourseFile document = new CourseFile();

        document.setName(fileName);
        document.setDescription(description);
        document.setType(contentType);
        document.setContent(file);
        document.setCourse(merged);

        em.persist(document);

        return document;
    }

    @Override
    public void deleteById(final long id) {
        LOGGER.trace("Deleting file with id {} ", id);
        CourseFile courseFile = em.find(CourseFile.class, id);
        if(courseFile != null) {
            em.remove(courseFile);
        }
    }
}
