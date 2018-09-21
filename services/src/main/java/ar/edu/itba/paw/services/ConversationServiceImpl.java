package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationDao conversationDao;

    @Override
    public boolean sendMessage(final User user, final Professor professor, final Subject subject, final String body) {
        final Conversation conversation = conversationDao.findByIds(user.getId(), professor.getId(), subject.getId());

        if(conversation == null) {
            final Conversation conversationCreated = conversationDao.create(user, professor, subject);
            return sendMessage(user, conversationCreated, body);
        } else
            return sendMessage(user, conversation, body);
    }

    @Override
    public boolean sendMessage(final User from, final Conversation conversation, final String body) {
        final Message message = conversationDao.create(from, body, conversation);

        return message != null;
    }

}
