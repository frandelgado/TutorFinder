package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SubjectDao;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SubjectJdbcDao implements SubjectDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Subject> ROW_MAPPER = (rs, rowNum) -> new Subject(
            rs.getLong("subject_id"),
            rs.getString("description"),
            rs.getString("name")
    );

    @Autowired
    public SubjectJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("subjects")
                .usingGeneratedKeyColumns("subject_id");
    }
    @Override
    public Optional<Subject> findById(final long id) {
        final List<Subject> list = jdbcTemplate.query(
                "SELECT * FROM subjects WHERE userid = ?", ROW_MAPPER, id
        );
        return list.stream().findFirst();
    }

    @Override
    public Subject create(String name, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("description", description);
        final Number subjectId = jdbcInsert.executeAndReturnKey(args);
        return new Subject(subjectId.longValue(), description, name);
    }
}