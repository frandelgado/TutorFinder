package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.models.Professor;
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
public class ProfessorJdbcDao implements ProfessorDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Professor> ROW_MAPPER = (rs, rowNum) -> new Professor(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5),
            rs.getString(6),
            rs.getString(7),
            rs.getBytes(8)
    );

    @Autowired
    public ProfessorJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("professors");
    }

    @Override
    public Professor create(final User user, final String description, final byte[] picture) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", user.getId());
        args.put("description", description);
        args.put("profile_picture", picture);
        jdbcInsert.execute(args);
        return new Professor(user.getId(), user.getUsername(), user.getName(),
                user.getLastname(), user.getPassword(), user.getEmail(), description, picture);
    }

    @Override
    public Optional<Professor> findById(final Long id) {
        final List<Professor> professors = jdbcTemplate.query(
                "SELECT users.user_id, username, name, lastname, password," +
                        " email, description, profile_picture FROM professors," +
                        " users WHERE users.user_id = ? AND users.user_id = professors.user_id"
                , ROW_MAPPER, id
        );
        return professors.stream().findFirst();
    }

    @Override
    public Optional<Professor> findByUsername(final String username) {
        final List<Professor> professors = jdbcTemplate.query(
                "SELECT users.user_id, username, name, lastname, password," +
                        " email, description, profile_picture FROM professors, users WHERE username = ?" +
                        "AND users.user_id = professors.user_id", ROW_MAPPER, username
        );
        return professors.stream().findFirst();
    }

    @Override
    public List<Professor> filterByFullName(final String fullName, final int limit, final int offset) {
        final String search = "%" + fullName + "%";
        final List<Professor> professors = jdbcTemplate.query(
                "SELECT users.user_id, username, name, lastname, password, email, " +
                        "description, profile_picture FROM professors, users WHERE " +
                        "UPPER (concat(name, ' ', lastname)) LIKE UPPER(?) " +
                        "AND users.user_id = professors.user_id ORDER BY professors.user_id LIMIT ? OFFSET ?",
                ROW_MAPPER, search, limit, offset
        );
        return professors;
    }
}
