package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.exceptions.SameUserConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ConversationServiceImpl implements ConversationService {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubjectService subjectService;

    @Transactional
    @Override
    public boolean sendMessage(final Long userId, final Long professorId, final Long subjectId, final String body)
            throws SameUserConversationException, UserNotInConversationException, NonexistentConversationException {

        final User user = userService.findUserById(userId);
        final Professor professor = professorService.findById(professorId);
        final Subject subject = subjectService.findSubjectById(subjectId);

        if(user == null || professor == null || subject == null || body == null || body.length() > 512)
            return false;

        if(user.getId().equals(professor.getId()))
            throw new SameUserConversationException();

        final Conversation conversation = conversationDao.findByIds(user.getId(), professor.getId(), subject.getId());

        if(conversation == null) {
            final Conversation conversationCreated = conversationDao.create(user, professor, subject);
            return sendMessage(user.getId(), conversationCreated.getId(), body);
        } else
            return sendMessage(user.getId(), conversation.getId(), body);
    }

    @Override
    public boolean sendMessage(final Long userId, final Long conversationId, final String body)
            throws UserNotInConversationException, NonexistentConversationException {

        final User from = userService.findUserById(userId);
        if(from == null) {
            return false;
        }
        final Conversation conversation = findById(conversationId, from.getId());

        if(from == null || !conversation.belongs(from.getId())) {
            throw new UserNotInConversationException();
        }

        if(body == null || body.isEmpty())
            return false;

        final Message message = conversationDao.create(from, body, conversation);

        return message != null;
    }


    @Override
    public PagedResults<Conversation> findByUserId(final Long userId, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            throw new PageOutOfBoundsException();
        }

        final List<Conversation> conversations = conversationDao.findByUserId(userId, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Conversation> results;
        final int size = conversations.size();

        if(size == 0 && page > 1) {
            throw new PageOutOfBoundsException();
        }

        if(size > PAGE_SIZE) {
            conversations.remove(PAGE_SIZE);
            results = new PagedResults<>(conversations, true);
        } else {
            results = new PagedResults<>(conversations, false);
        }
        return results;
    }

    @Transactional
    @Override
    public Conversation findById(final Long conversationId, final Long userId)
            throws UserNotInConversationException, NonexistentConversationException {
        final Conversation conversation = conversationDao.findById(conversationId);

        if(conversation == null)
            throw new NonexistentConversationException();

        if(!conversation.belongs(userId))
            throw new UserNotInConversationException();

        return conversation;
    }

}
