package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProfessorJdbcDao implements ProfessorDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Professor> ROW_MAPPER = (rs, rowNum) -> new Professor(
            new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email")
            ),
            rs.getString("description")
    );

    @Autowired
    public ProfessorJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("professors");
    }

    @Override
    public Professor create(User user, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", user.getId());
        args.put("description", description);
        jdbcInsert.execute(args);
        return new Professor(user, description);
    }
}
