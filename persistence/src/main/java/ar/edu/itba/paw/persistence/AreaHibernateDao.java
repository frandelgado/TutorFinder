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
        final String search = "%" + name + "%";
        final TypedQuery<Area> query = em.createQuery("from Area as a where upper(a.name) like upper(:name)" +
                "order by a.id", Area.class);
        query.setParameter("name", search);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
