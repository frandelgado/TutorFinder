package ar.edu.itba.paw.services;

import ar.edu.itba.exceptions.NonexistentConversationException;
import ar.edu.itba.exceptions.SameUserConversationException;
import ar.edu.itba.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationDao conversationDao;

    @Override
    public boolean sendMessage(final User user, final Professor professor, final Subject subject, final String body)
            throws SameUserConversationException, UserNotInConversationException {
        if(user.getId().equals(professor.getId()))
            throw new SameUserConversationException();

        final Conversation conversation = conversationDao.findByIds(user.getId(), professor.getId(), subject.getId());

        if(conversation == null) {
            final Conversation conversationCreated = conversationDao.create(user, professor, subject);
            return sendMessage(user, conversationCreated, body);
        } else
            return sendMessage(user, conversation, body);
    }

    @Override
    public boolean sendMessage(final User from, final Conversation conversation, final String body)
            throws UserNotInConversationException {

        if(!conversation.belongs(from.getId())) {
            throw new UserNotInConversationException();
        }

        final Message message = conversationDao.create(from, body, conversation);

        return message != null;
    }

    public List<Conversation> findByUserId(final Long userId) {
        return conversationDao.findByUserId(userId);
    }

    @Override
    public Conversation findById(final Long conversation_id, final Long userId)
            throws UserNotInConversationException, NonexistentConversationException {
        final Conversation conversation = conversationDao.findById(conversation_id);

        if(conversation == null)
            throw new NonexistentConversationException();

        if(!conversation.belongs(userId))
            throw new UserNotInConversationException();

        return conversation;
    }

}
