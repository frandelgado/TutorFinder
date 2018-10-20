package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SubjectDao;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.Subject;
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
public class SubjectJdbcDao implements SubjectDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Subject> ROW_MAPPER = (rs, rowNum) -> new Subject(
            rs.getLong(1),
            rs.getString(3),
            rs.getString(2),
            new Area(
                    rs.getString(6),
                    rs.getString(5),
                    rs.getBytes(7)
                    )
    );

    private final static RowMapper<Area> AREA_ROW_MAPPER= (rs, rowNum) -> new Area(
            rs.getString(3),
            rs.getString(2),
            rs.getBytes(4)
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
        final List<Subject> subjects = jdbcTemplate.query(
                "SELECT subject_id, subjects.name as subjects_name, " +
                        "subjects.description as subject_description, " +
                        "subjects.area_id as area_id, areas.name as areas_name, " +
                        "areas.description as areas_description, areas.image FROM subjects, areas " +
                        "WHERE subject_id = ? AND areas.area_id=subjects.area_id;", ROW_MAPPER, id
        );
        return subjects.stream().findFirst();
    }

    @Override
    public Subject create(final String name, final String description, final Long area_id) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("description", description);
        args.put("area_id", area_id);
        final Number subjectId = jdbcInsert.executeAndReturnKey(args);
        final Area area = jdbcTemplate.query(
                "SELECT area_id, name, description, image FROM areas where area_id = ?", AREA_ROW_MAPPER, area_id
        ).stream().findFirst().orElse(null);
        return new Subject(subjectId.longValue(), description, name, area);
    }

    @Override
    public List<Subject> filterSubjectsByName(final String name) {
        final String search = "%" + name + "%";

        final List<Subject> subjects = jdbcTemplate.query(
                "SELECT subject_id, subjects.name as subjects_name, " +
                        "subjects.description as subject_description, " +
                        "subjects.area_id as area_id, areas.name as areas_name, " +
                        "areas.description as areas_description, areas.image FROM subjects, areas " +
                        "WHERE UPPER(subjects.name) LIKE UPPER(?) AND subjects.area_id = areas.area_id", ROW_MAPPER, search
        );
        return subjects;
    }

    @Override
    public List<Subject> getAvailableSubjects(final long id) {

        final List<Subject> subjects = jdbcTemplate.query(
                "SELECT subjects.subject_id, subjects.name as subjects_name, " +
                        "subjects.description as subject_description, " +
                        "subjects.area_id as area_id, areas.name as areas_name, " +
                        "areas.description as areas_description, areas.image FROM subjects, areas " +
                        "WHERE subjects.area_id = areas.area_id AND NOT EXISTS " +
                        "(SELECT * FROM courses WHERE courses.subject_id = subjects.subject_id " +
                        "AND courses.user_id = ?)", ROW_MAPPER, id
        );
        return subjects;
    }
}