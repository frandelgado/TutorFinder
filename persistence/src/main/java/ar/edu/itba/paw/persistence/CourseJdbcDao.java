package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
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
public class CourseJdbcDao implements CourseDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final String COURSES_SELECT_FROM = "SELECT courses.user_id, courses.subject_id," +
            "courses.description, price, rating, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id " +
            "FROM courses, professors, users, subjects, areas ";

    private final static RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(
            new Professor(
                    rs.getLong(1),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(11),
                    rs.getString(6)
                    ),
            new Subject(
                    rs.getLong(2),
                    rs.getString(12),
                    rs.getString(13)
                    ),
            rs.getString(3),
            rs.getDouble(4),
            rs.getDouble(5)
    );

    @Autowired
    public CourseJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("courses");
    }

    @Override
    public Optional<Course> findByIds(final long professor_id, final long subject_id) {
        final List<Course> list = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = ? AND courses.subject_id = ? " +
                        "AND professors.user_id = users.user_id AND areas.area_id = subjects.area_id " +
                        "AND users.user_id = courses.user_id AND courses.subject_id = subjects.subject_id"
                , ROW_MAPPER, professor_id, subject_id);
        return list.stream().findFirst();
    }

    @Override
    public List<Course> filterCoursesByName(final String name) {
        final String search = "%" + name + "%";
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = users.user_id AND" +
                        " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
                        "AND areas.area_id = subjects.area_id AND UPPER(subjects.name) LIKE UPPER(?)"
                , ROW_MAPPER, search
        );
        return courses;
    }
    }
}