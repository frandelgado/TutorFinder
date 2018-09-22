package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class ConversationJdbcDaoTest {

    private static final Long SUBJECT_ID = 1l;
    private static final Long PROFESSOR_ID = 2l;
    private static final Long USER_ID = 3l;
    private static final Long SENDER_1 = 2l;
    private static final Long SENDER_2 = 3l;
    private static final String BODY_1 = "Hola";
    private static final String BODY_2 = "Hola2";
    private static final Long CONVERSATION_ID = 1l;
    private static final int CONVERSATION_NUMBER = 2;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConversationJdbcDao conversationDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testCreateValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "conversations");
        Professor mockProfessor = mock(Professor.class);
        User mockUser = mock(User.class);
        Subject mockSubject = mock(Subject.class);
        when(mockProfessor.getId()).thenReturn(PROFESSOR_ID);
        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockSubject.getId()).thenReturn(SUBJECT_ID);

        final Conversation conversation = conversationDao.create(mockUser, mockProfessor, mockSubject);

        assertNotNull(conversation);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "conversations",
                "user_id = " + USER_ID + " AND professor_id = " + PROFESSOR_ID +
                        " AND subject_id = " + SUBJECT_ID));

    }

    @Test
    public void testfindByIdValid() {

        final Conversation conversation = conversationDao.findById(CONVERSATION_ID);

        assertNotNull(conversation);
        final Message message1 = conversation.getMessages().get(0);
        final Message message2 = conversation.getMessages().get(1);

        assertEquals(BODY_1, message1.getText());
        assertEquals(SENDER_1, message1.getSender().getId());
        assertEquals(BODY_2, message2.getText());
        assertEquals(SENDER_2, message2.getSender().getId());
    }

    @Test
    public void testfindByUserIdValid() {

        final List<Conversation> conversations = conversationDao.findByUserId(PROFESSOR_ID);

        assertNotNull(conversations);
        assertEquals(CONVERSATION_NUMBER, conversations.size());
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "courses");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "conversations");
    }
}
