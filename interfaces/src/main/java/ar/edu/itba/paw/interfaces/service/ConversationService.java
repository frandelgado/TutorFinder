package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.exceptions.NonexistentConversationException;
import ar.edu.itba.exceptions.SameUserConversationException;
import ar.edu.itba.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface ConversationService {

    boolean sendMessage(final User user, final Professor professor, final Subject subject, final String body)
            throws SameUserConversationException, UserNotInConversationException;

    boolean sendMessage(final User from, final Conversation conversation, final String body) throws UserNotInConversationException;

    List<Conversation> findByUserId(final Long userId);

    Conversation findById(final Long conversation_id, final  Long userId) throws UserNotInConversationException, NonexistentConversationException;
}
