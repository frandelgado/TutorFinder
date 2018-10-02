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
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ConversationServiceImpl implements ConversationService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationServiceImpl.class);

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public boolean sendMessage(final Long userId, final Long professorId, final Long subjectId, final String body)
            throws SameUserConversationException, UserNotInConversationException, NonexistentConversationException {

        final User user = userService.findUserById(userId);
        final Professor professor = professorService.findById(professorId);
        final Subject subject = subjectService.findSubjectById(subjectId);

        if(user == null || professor == null || subject == null) {
            LOGGER.error("Attempted to send message with invalid parameters");
            return false;
        }

        if(body == null || body.length() < 1 || body.length() > 1024) {
            LOGGER.error("Attempted to send message invalid body size");
            return false;
        }

        if(user.getId().equals(professor.getId())) {
            LOGGER.error("User attempted to send message to himself");
            throw new SameUserConversationException();
        }

        LOGGER.debug("Searching for conversation belonging to user with id {} and professor with id {} about subject with id {}",
                userId, professorId, subjectId);
        final Conversation conversation = conversationDao.findByIds(user.getId(), professor.getId(), subject.getId());

        if(conversation == null) {
            LOGGER.debug("Conversation belonging to user with id {} and professor with id {} about subject with id {} does not exist, " +
                    "creating conversation", userId, professorId, subjectId);
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
            LOGGER.error("Attempted to send message from non existent user");
            return false;
        }
        final Conversation conversation = findById(conversationId, from.getId());

        if(conversation == null || !conversation.belongs(from.getId())) {
            LOGGER.error("Attempted to send message in conversation with id {} from a user that is not part of it",
                    conversationId);
            throw new UserNotInConversationException();
        }

        if(body == null || body.length() < 1 || body.length() > 1024) {
            LOGGER.error("Attempted to send message invalid body size");
            return false;
        }

        LOGGER.debug("Sending message from user with id {} in conversation with id {}", userId, conversationId);
        final Message message = conversationDao.create(from, body, conversation);

        final User to = conversation.getProfessor().getId().equals(from.getId())
                ? conversation.getUser(): conversation.getProfessor();

        if(message != null) {
            LOGGER.debug("Sending contact email from user with id {} to user with id {} in conversation with id {}",
                    userId, to.getId(), conversationId);
            emailService.sendContactEmail(from, to, conversation);
        }

        return message != null;
    }


    @Override
    public PagedResults<Conversation> findByUserId(final Long userId, final int page) throws PageOutOfBoundsException {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            throw new PageOutOfBoundsException();
        }

        LOGGER.debug("Searching for conversations belonging to user with id {}", userId);
        final List<Conversation> conversations = conversationDao.findByUserId(userId, PAGE_SIZE + 1, PAGE_SIZE * (page - 1));
        final PagedResults<Conversation> results;
        final int size = conversations.size();

        if(size == 0 && page > 1) {
            LOGGER.error("Page number exceeds total page count");
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
        LOGGER.debug("Searching for conversation with id {}", conversationId);
        final Conversation conversation = conversationDao.findById(conversationId);

        if(conversation == null) {
            LOGGER.error("Attempted to get a non existent conversation");
            throw new NonexistentConversationException();
        }

        if(!conversation.belongs(userId)) {
            LOGGER.error("User with id {} attempted to get a conversation that does not belong to him");
            throw new UserNotInConversationException();
        }

        return conversation;
    }

}
