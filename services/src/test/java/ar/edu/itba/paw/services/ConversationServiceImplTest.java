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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ConversationServiceImplTest {

    private static final int PAGE_SIZE = 5;
    private static final Long ID = 1L;
    private static final Long USER_ID = 7L;
    private static final Long PROFESSOR_ID = 5L;
    private static final Long SUBJECT_ID = 9L;
    private static final Long INVALID_ID = 666L;
    private static final String BODY = "Omne duo vim sum nudi uno quod. Latera nullam ad realem passim ii essent ut patere.";

    @Spy
    @InjectMocks
    @Autowired
    private ConversationService conversationService;

    @Mock
    private ConversationDao conversationDao;

    @Mock
    private UserService userService;

    @Mock
    private ProfessorService professorService;

    @Mock
    private SubjectService subjectService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByUserIdHasNext() throws PageOutOfBoundsException {
        final List<Conversation> conversations = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            conversations.add(mock(Conversation.class));
        }
        when(conversationDao.findByUserId(eq(ID), anyInt(), anyInt())).thenReturn(conversations);

        final PagedResults<Conversation> results = conversationService.findByUserId(ID, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFindByUserIdNoNext() throws PageOutOfBoundsException {
        final List<Conversation> conversations = new LinkedList<>();
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        final Integer PAGE = 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            conversations.add(mock(Conversation.class));
        }
        when(conversationDao.findByUserId(eq(ID), anyInt(), anyInt())).thenReturn(conversations);

        final PagedResults<Conversation> results = conversationService.findByUserId(ID, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFindByUserIdPageOutOfBounds() throws PageOutOfBoundsException {
        final List<Conversation> conversations = mock(List.class);
        when(conversations.size()).thenReturn(0);
        when(conversationDao.findByUserId(eq(ID), anyInt(), anyInt())).thenReturn(conversations);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Conversation> results = conversationService.findByUserId(ID, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFindByUserIdNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Conversation> results = conversationService.findByUserId(ID, INVALID_PAGE);
    }

    @Test
    public void testFindByIdValid() throws NonexistentConversationException, UserNotInConversationException {
        final Conversation conversationMock = mock(Conversation.class);
        when(conversationMock.belongs(USER_ID)).thenReturn(true);
        when(conversationDao.findById(ID)).thenReturn(conversationMock);

        final Conversation conversation = conversationService.findById(ID, USER_ID);
        assertNotNull(conversation);
    }

    @Test(expected = NonexistentConversationException.class)
    public void testFindByIdInvalidConversation() throws NonexistentConversationException, UserNotInConversationException {

        when(conversationDao.findById(INVALID_ID)).thenReturn(null);

        final Conversation conversation = conversationService.findById(INVALID_ID, USER_ID);
    }

    @Test(expected = UserNotInConversationException.class)
    public void testFindByIdUserNotInConversation() throws NonexistentConversationException, UserNotInConversationException {
        final Conversation conversationMock = mock(Conversation.class);
        when(conversationMock.belongs(INVALID_ID)).thenReturn(false);
        when(conversationDao.findById(ID)).thenReturn(conversationMock);

        final Conversation conversation = conversationService.findById(ID, INVALID_ID);
    }

    @Test
    public void testSendMessageWithConversationValid() throws NonexistentConversationException, UserNotInConversationException {

        final User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final Conversation conversation = mock(Conversation.class);
        when(conversation.belongs(USER_ID)).thenReturn(true);
        when(conversation.getId()).thenReturn(ID);
        doReturn(conversation).when(conversationService).findById(ID, USER_ID);

        when(conversationDao.create(user, BODY, conversation)).thenReturn(mock(Message.class));

        final boolean sent = conversationService.sendMessage(USER_ID, ID, BODY);
        assertTrue(sent);
    }

    @Test
    public void testSendMessageWithConversationInvalidUser() throws NonexistentConversationException, UserNotInConversationException {

        when(userService.findUserById(INVALID_ID)).thenReturn(null);

        final boolean sent = conversationService.sendMessage(INVALID_ID, ID, BODY);
        assertFalse(sent);
    }

    @Test
    public void testSendMessageWithConversationInvalidBody() throws NonexistentConversationException, UserNotInConversationException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final Conversation conversation = mock(Conversation.class);
        when(conversation.belongs(USER_ID)).thenReturn(true);
        when(conversation.getId()).thenReturn(ID);
        doReturn(conversation).when(conversationService).findById(ID, USER_ID);

        final boolean sent = conversationService.sendMessage(USER_ID, ID, "");
        assertFalse(sent);
    }

    @Test
    public void testSendMessageValid() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        final Conversation conversation = mock(Conversation.class);
        when(conversation.getId()).thenReturn(ID);
        when(conversationDao.findByIds(USER_ID, PROFESSOR_ID, SUBJECT_ID)).thenReturn(conversation);

        doReturn(true).when(conversationService).sendMessage(USER_ID, ID, BODY);

        final boolean sent = conversationService.sendMessage(USER_ID, PROFESSOR_ID, SUBJECT_ID, BODY);
        assertTrue(sent);
    }

    @Test
    public void testSendMessageInvalidUser() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        when(userService.findUserById(INVALID_ID)).thenReturn(null);
        when(professorService.findById(PROFESSOR_ID)).thenReturn(mock(Professor.class));
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(mock(Subject.class));

        final boolean sent = conversationService.sendMessage(INVALID_ID, PROFESSOR_ID, SUBJECT_ID, BODY);
        assertFalse(sent);
    }

    @Test
    public void testSendMessageInvalidSubject() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        when(userService.findUserById(USER_ID)).thenReturn(mock(User.class));
        when(professorService.findById(PROFESSOR_ID)).thenReturn(mock(Professor.class));
        when(subjectService.findSubjectById(INVALID_ID)).thenReturn(null);

        final boolean sent = conversationService.sendMessage(USER_ID, PROFESSOR_ID, INVALID_ID, BODY);
        assertFalse(sent);
    }

    @Test
    public void testSendMessageInvalidProfessor() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        when(userService.findUserById(USER_ID)).thenReturn(mock(User.class));
        when(professorService.findById(INVALID_ID)).thenReturn(null);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(mock(Subject.class));

        final boolean sent = conversationService.sendMessage(USER_ID, INVALID_ID, SUBJECT_ID, BODY);
        assertFalse(sent);
    }

    @Test(expected = SameUserConversationException.class)
    public void testSendMessageSameUserConversation() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(USER_ID);
        when(professorService.findById(USER_ID)).thenReturn(professor);

        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(mock(Subject.class));

        final boolean sent = conversationService.sendMessage(USER_ID, USER_ID, SUBJECT_ID, BODY);
    }

    @Test
    public void testSendMessageCreateConversation() throws NonexistentConversationException, UserNotInConversationException, SameUserConversationException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user);

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        when(conversationDao.findByIds(USER_ID, PROFESSOR_ID, SUBJECT_ID)).thenReturn(null);

        final Conversation conversation = mock(Conversation.class);
        when(conversation.getId()).thenReturn(ID);
        when(conversationDao.create(user, professor, subject)).thenReturn(conversation);

        doReturn(true).when(conversationService).sendMessage(USER_ID, ID, BODY);

        final boolean sent = conversationService.sendMessage(USER_ID, PROFESSOR_ID, SUBJECT_ID, BODY);
        assertTrue(sent);
    }


}
