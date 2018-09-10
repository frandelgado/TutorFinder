package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("user_id"),
            rs.getString("username"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email")
    );

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Optional<User> findById(long id) {
        final List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE user_id = ?", ROW_MAPPER, id
        );
        return users.stream().findFirst();
    }

    @Override
    public User create(String username, String password, String email, String name, String lastName) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username", username);
        args.put("password", password);
        args.put("email", email);
        args.put("name", name);
        args.put("lastname", lastName);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), username, name, lastName, password, email);
    }
}
