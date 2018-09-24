package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
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
public class CourseJdbcDao implements CourseDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final String COURSES_SELECT_FROM = "SELECT courses.user_id, courses.subject_id," +
            "courses.description, price, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id " +
            "FROM courses, professors, users, subjects, areas ";

    private static final String COURSES_SELECT_FROM_TIMESLOT = COURSES_SELECT_FROM+", schedules ";

    private final static RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(
            new Professor(
                    rs.getLong(1),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(5)
                    ),
            new Subject(
                    rs.getLong(2),
                    rs.getString(11),
                    rs.getString(12),
                    new Area(
                            rs.getLong(15),
                            rs.getString(14),
                            rs.getString(13)
                            )
                    ),
            rs.getString(3),
            rs.getDouble(4)
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
    public List<Course> findByProfessorId(long professor_id) {
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id " +
                        "AND courses.subject_id = subjects.subject_id", ROW_MAPPER, professor_id
        );
        return courses;
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

    @Override
    public List<Course> filterByAreaId(final long areaId) {
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE subjects.area_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id" +
                        " AND courses.subject_id = subjects.subject_id"
                , ROW_MAPPER, areaId);
        return courses;
    }

    @Override
    public List<Course> filterCoursesByTimeAndProfessor(final int day, final int startHour, final int endHour, final long professor_id) {
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM_TIMESLOT + "WHERE courses.user_id = users.user_id AND" +
                        " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
                        "AND areas.area_id = subjects.area_id AND schedules.day = ? AND schedules.hour >= ? AND schedules.hour < ? AND courses.user_id = ?",
                ROW_MAPPER, new Object[]{day, startHour, endHour, professor_id});
        return courses;
    }

    @Override
    public Course create(final Professor professor, final Subject subject, final String description,
                         final Double price) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", professor.getId());
        args.put("subject_id", subject.getId());
        args.put("description", description);
        args.put("price", price);
        try {
            jdbcInsert.execute(args);
        } catch (DuplicateKeyException e) {
            return null;
        }
        return new Course(professor, subject, description, price);
    }
}