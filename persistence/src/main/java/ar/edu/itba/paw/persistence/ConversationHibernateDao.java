package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ConversationHibernateDao implements ConversationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Conversation create(User user, Professor professor, Subject subject) {
        return null;
    }

    @Override
    public Conversation findById(Long conversation_id) {
        return null;
    }

    @Override
    public List<Conversation> findByUserId(Long user_id, int limit, int offset) {
        return new LinkedList<>();
    }

    @Override
    public Conversation findByIds(Long user_id, Long professor_id, Long subject_id) {
        return null;
    }

    @Override
    public Message create(User sender, String text, Conversation conversation) {
        return null;
    }
}
