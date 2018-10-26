package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;

import java.util.List;

public interface ConversationDao {

    Conversation create(final User user, final Professor professor, final Subject subject);

    Conversation findById(final Long conversation_id);

    List<Conversation> findByUserId(final Long user_id, final int limit, final int offset);

    Conversation findByIds(final Long user_id, final Long professor_id, final Long subject_id);

    Conversation merge(Conversation conversation);

    Message create(final User sender, final String text, final Conversation conversation);
}
