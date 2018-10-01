package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcDao.class);

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5),
            rs.getString(6)
    );

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Optional<User> findById(final long id) {
        LOGGER.trace("Querying for user with id {}", id);
        final List<User> users = jdbcTemplate.query(
                "SELECT user_id, username, name, lastname, password," +
                        " email FROM users WHERE user_id = ?", ROW_MAPPER, id
        );
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        LOGGER.trace("Querying for user with username {}", username);
        final List<User> users = jdbcTemplate.query(
                "SELECT user_id, username, name, lastname, password," +
                        "email FROM users WHERE username = ?", ROW_MAPPER, username
        );
        return users.stream().findFirst();
    }

    @Override
    public boolean changePasswordById(Long userId, String newPassword) {
        LOGGER.trace("Changing password for user with id {}", userId);
        final int updated = jdbcTemplate.update("UPDATE users SET password = ? WHERE user_id = ?",
                newPassword, userId);
        return updated != 0;
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        LOGGER.trace("Querying for user with email {}", email);
        final List<User> users = jdbcTemplate.query(
                "SELECT user_id, username, name, lastname, password," +
                        "email FROM users WHERE email = ?", ROW_MAPPER, email
        );
        return users.stream().findFirst();
    }

    @Override
    public User create(final String username, final String password, final String email,
                       final String name, final String lastName)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final Map<String, Object> args = new HashMap<>();
        final Number userId;
        args.put("username", username);
        args.put("password", password);
        args.put("email", email);
        args.put("name", name);
        args.put("lastname", lastName);
        try {
            LOGGER.trace("Inserting user with username {}", username);
            userId = jdbcInsert.executeAndReturnKey(args);
        } catch (DuplicateKeyException e) {
            if(findByUsername(username).isPresent()) {
                if (findByEmail(email).isPresent()) {
                    LOGGER.error("User with username {} and email {} already exists", username, email);
                    throw new UsernameAndEmailAlreadyInUseException();
                }
                LOGGER.error("User with username {} already exists", username);
                throw new UsernameAlreadyInUseException();
            }
            if(findByEmail(email).isPresent()) {
                LOGGER.error("User with email {} already exists", email);
                throw new EmailAlreadyInUseException();
            }
            return null;
        }
        return new User(userId.longValue(), username, name, lastName, password, email);
    }
}
