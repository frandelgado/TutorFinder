package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.models.*;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationJdbcDao implements ConversationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationJdbcDao.class);

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertConversation;
    private final SimpleJdbcInsert jdbcInsertMessage;

    private static final String CONVERSATION_SELECT_FROM = "SELECT conversations.conversation_id, " +
            "u.user_id, u.username, u.name, u.lastname, u.password, u.email," +
            " p.user_id, p.username, p.name, p.lastname, p.password, p.email, professors.description," +
            " subjects.subject_id, subjects.description, subjects.name, " +
            "areas.area_id, areas.description, areas.name, m.latest, professors.profile_picture, areas.image " +
            "FROM users as u, users as p, professors, subjects, areas, conversations LEFT OUTER JOIN " +
            "(SELECT max(created) AS latest, conversation_id AS c_id FROM messages GROUP BY" +
            " conversation_id) AS m ON m.c_id = conversations.conversation_id ";

    private final static RowMapper<Conversation> CONVERSATION_ROW_MAPPER = (rs, rowNum) -> {
        return new Conversation(
                rs.getLong(1),
                new User(rs.getLong(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7)
                ),
                new Professor(
                        rs.getLong(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getString(14),
                        rs.getBytes(22)
                ),
                new Subject(
                        rs.getLong(15),
                        rs.getString(16),
                        rs.getString(17),
                        new Area(
                                rs.getString(19),
                                rs.getString(20),
                                rs.getBytes(23)
                        )
                ),
                new LocalDateTime(rs.getTimestamp(21)));
    };

    private final static RowMapper<Message> MESSAGE_ROW_MAPPER = (rs, rowNum) -> {
        return new Message(
                rs.getLong(7),
                new User(rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                ),
                rs.getString(8),
                new LocalDateTime(rs.getTimestamp(9))
        );
    };

    @Autowired
    public ConversationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertConversation = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("conversations")
                .usingGeneratedKeyColumns("conversation_id");
        jdbcInsertMessage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("messages")
                .usingGeneratedKeyColumns("message_id");
    }

    @Override
    public Conversation create(final User user, final Professor professor, final Subject subject) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", user.getId());
        args.put("professor_id", professor.getId());
        args.put("subject_id", subject.getId());
        LOGGER.trace("Inserting conversation with user_id {}, professor_id {} and subject_id {}", user.getId(),
                professor.getId(), subject.getId());
        final Number id = jdbcInsertConversation.executeAndReturnKey(args);

        return new Conversation(id.longValue(), user, professor, subject, null);
    }

    @Override
    public Message create(final User sender, final String text, final Conversation conversation) {
        final Map<String, Object> args = new HashMap<>();
        final LocalDateTime currentTime = LocalDateTime.now();
        args.put("sender_id", sender.getId());
        args.put("conversation_id", conversation.getId());
        args.put("message", text);
        args.put("created", new Timestamp(currentTime.toDateTime().getMillis()));
        LOGGER.trace("Inserting message with sender_id {} into conversation with id {}", sender.getId(),
                conversation.getId());
        final Number id = jdbcInsertMessage.executeAndReturnKey(args);
        return new Message(id.longValue(), sender, text, currentTime);
    }

    @Override
    public Conversation findById(final Long conversation_id) {
        LOGGER.trace("Querying for conversation with id {}", conversation_id);
       final Conversation conversation = jdbcTemplate.query(
               CONVERSATION_SELECT_FROM + "WHERE conversations.conversation_id = ? " +
                       "AND conversations.user_id = u.user_id AND conversations.professor_id = p.user_id AND " +
                       "areas.area_id = subjects.area_id AND conversations.subject_id = subjects.subject_id " +
                       "AND p.user_id = professors.user_id"
                , CONVERSATION_ROW_MAPPER, conversation_id).stream().findFirst().orElse(null);

       if(conversation != null) {
           LOGGER.trace("Adding messages for conversation with id {}", conversation_id);
           conversation.addMessages(getMessagesByConversationId(conversation.getId()));
       }
       return conversation;
    }

    @Override
    public List<Conversation> findByUserId(final Long user_id, final int limit, final int offset) {
        LOGGER.trace("Querying for conversations belonging to a user with id {}", user_id);
        final List<Conversation> conversations = jdbcTemplate.query(
                CONVERSATION_SELECT_FROM + "WHERE (conversations.user_id = ? OR " +
                        "conversations.professor_id = ?) AND conversations.user_id = u.user_id" +
                        " AND conversations.professor_id = p.user_id AND areas.area_id = subjects.area_id" +
                        " AND conversations.subject_id = subjects.subject_id AND p.user_id = professors.user_id" +
                        " ORDER BY conversations.conversation_id LIMIT ? OFFSET ?"
                , CONVERSATION_ROW_MAPPER, user_id, user_id, limit, offset);

        return conversations;
    }

    @Override
    public Conversation findByIds(final Long user_id, final Long professor_id, final Long subject_id) {
        LOGGER.trace("Querying for conversation belonging to the users with id {} and {} for subject with id {}",
                user_id, professor_id, subject_id);
        final List<Conversation> conversations = jdbcTemplate.query(
                CONVERSATION_SELECT_FROM + "WHERE conversations.user_id = ? AND " +
                        "conversations.professor_id = ? AND conversations.subject_id = ? AND " +
                        "conversations.user_id = u.user_id AND conversations.professor_id = p.user_id" +
                        " AND areas.area_id = subjects.area_id AND conversations.subject_id = subjects.subject_id" +
                        " AND p.user_id = professors.user_id"
                , CONVERSATION_ROW_MAPPER, user_id, professor_id, subject_id);

        return conversations.stream().findFirst().orElse(null);
    }

    @Override
    public Conversation merge(Conversation conversation) {
        return null;
    }

    private List<Message> getMessagesByConversationId(final Long conversation_id) {
        LOGGER.trace("Getting messages for conversation with id {}", conversation_id);
        final List<Message> messages = jdbcTemplate.query("SELECT users.user_id, users.username, " +
                        "users.name, users.lastname, users.password, users.email, messages.message_id," +
                        " messages.message, messages.created FROM users, messages " +
                        "WHERE users.user_id = messages.sender_id AND messages.conversation_id = ? " +
                        "ORDER BY messages.created", MESSAGE_ROW_MAPPER, conversation_id);
        return messages;
    }
}