package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PasswordResetTokenDao;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PasswordResetTokenJdbcDao implements PasswordResetTokenDao {

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<PasswordResetToken> ROW_MAPPER = (rs, rowNum) -> new PasswordResetToken(
            rs.getLong(1),
            new User(
                    rs.getLong(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
            ),
            rs.getString(8),
            new LocalDateTime(rs.getTimestamp(9))
    );

    @Autowired
    public PasswordResetTokenJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reset_password_tokens")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public PasswordResetToken findByToken(final String token) {
        final List<PasswordResetToken> tokens = jdbcTemplate.query(
                "SELECT id, user_id, username, name, lastname, password," +
                        " email, token, expires FROM reset_password_tokens NATURAL JOIN users " +
                        " WHERE token = ? AND expires >= NOW()", ROW_MAPPER, token
        );
        return tokens.stream().findFirst().orElse(null);
    }

    @Override
    public PasswordResetToken create(final Long userId, final String token, final LocalDateTime expires) {
        final Map<String, Object> args = new HashMap<>();
        final Number id;
        args.put("user_id", userId);
        args.put("token", token);
        args.put("expires", new Timestamp(expires.toDateTime().getMillis()));
        try {
            id = jdbcInsert.executeAndReturnKey(args);
        } catch (DataIntegrityViolationException e) {
            return null;
        }
        return new PasswordResetToken(id.longValue(), null, token, expires);
    }

    @Override
    public void purgeExpiredTokens() {
        jdbcTemplate.update("DELETE FROM reset_password_tokens WHERE expires < NOW()");
    }

    @Override
    public void deleteUsedToken(String token) {
        jdbcTemplate.update("DELETE FROM reset_password_tokens WHERE token = ?", token);
    }
}
