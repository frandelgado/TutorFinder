package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.models.Area;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AreaHibernateDao implements AreaDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Area findById(long id) {
        return em.find(Area.class, id);
    }

    @Override
    public Area create(String name, String description, byte[] image) {
        final Area area = new Area(description, name, image);
        em.persist(area);
        return area;
    }

    //TODO: ver como vamos a manejar paginación con hibernate. Esto es un primer approach
    @Override
    public List<Area> filterAreasByName(String name, int limit, int offset) {
        final TypedQuery<Area> query = em.createQuery("select a from Area as a where u.name = :name", Area.class);
        query.setParameter("name", name);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        final List<Area> areas = query.getResultList();
        return areas.isEmpty() ? null : areas;

    }
}
