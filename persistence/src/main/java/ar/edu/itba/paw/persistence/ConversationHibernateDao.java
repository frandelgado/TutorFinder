package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.models.*;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ConversationHibernateDao implements ConversationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Conversation create(User user, Professor professor, Subject subject) {
        final Conversation conversation = new Conversation(user, professor, subject, null);
        em.persist(conversation);
        return conversation;
    }

    @Override
    public Conversation findById(Long conversation_id) {
        return em.find(Conversation.class, conversation_id);
    }

    @Override
    public List<Conversation> findByUserId(Long user_id, int limit, int offset) {
        final TypedQuery<Conversation> query = em.createQuery("from Conversation as c where" +
                " c.professor.id = :id or c.user.id = :id order by c.id", Conversation.class);
        query.setParameter("id", user_id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Conversation findByIds(Long user_id, Long professor_id, Long subject_id) {
        final TypedQuery<Conversation> query = em.createQuery("from Conversation as c " +
                "where c.user.id = :user_id and c.professor.id = :professor_id and " +
                "c.subject.id = :subject_id", Conversation.class);
        query.setParameter("user_id", user_id);
        query.setParameter("professor_id", professor_id);
        query.setParameter("subject_id", subject_id);
        final List<Conversation> conversations = query.getResultList();
        return conversations.isEmpty()? null : conversations.get(0);
    }

    @Override
    public Conversation merge(final Conversation conversation) {
        return em.merge(conversation);
    }

    @Override
    public Message create(User sender, String text, Conversation conversation) {
        final LocalDateTime currentTime = LocalDateTime.now();
        final Message message = new Message(sender, text, currentTime);
        em.persist(message);
        try {
            em.flush();
        } catch (PersistenceException e) {
            return null;
        }
        return message;
    }
}
