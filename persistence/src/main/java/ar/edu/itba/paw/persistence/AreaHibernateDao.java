package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.persistence.utils.InputSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AreaHibernateDao implements AreaDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InputSanitizer inputSanitizer;

    @Override
    public Area findById(final long id) {
        LOGGER.trace("Querying for area with id {}", id);
        return em.find(Area.class, id);
    }

    @Override
    public Area create(final String name, final String description, final byte[] image) {
        final Area area = new Area(description, name, image);
        em.persist(area);
        return area;
    }

    @Override
    public List<Area> filterAreasByName(final String name, final int limit, final int offset) {
        final String search = "%" + inputSanitizer.sanitizeWildcards(name) + "%";
        LOGGER.trace("Querying for areas with name containing {}", name);
        final TypedQuery<Area> query = em.createQuery("from Area as a where upper(a.name) like upper(:name)" +
                "order by a.id", Area.class);
        query.setParameter("name", search);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
