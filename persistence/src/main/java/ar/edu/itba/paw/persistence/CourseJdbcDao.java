package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseJdbcDao implements CourseDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    //TODO: Change nulls to new professor and subject
    private final static RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(null, null,
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getDouble("rating")
    );

    @Autowired
    public CourseJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("courses")
                .usingGeneratedKeyColumns("professor_id", "subject_id");
    }

    @Override
    public Optional<Course> findByIds(final long professor_id, final long subject_id) {
        final List<Course> list = jdbcTemplate.query(
                "SELECT * FROM courses WHERE user_id = ? AND subject_id = ?", ROW_MAPPER,
                professor_id, subject_id);
        return list.stream().findFirst();
    }

    @Override
    public List<Course> filterCoursesByName(String name) {
        final String search = "%" + name + "%";
        final List<Course> list = jdbcTemplate.query(
                "SELECT * FROM courses, subjects WHERE courses.subject_id = subjects.subject_id " +
                        "AND UPPER(subjects.name) LIKE UPPER(?)", ROW_MAPPER, search
        );
        return list;
    }
}