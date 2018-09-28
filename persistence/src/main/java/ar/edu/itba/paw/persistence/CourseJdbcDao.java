package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CourseJdbcDao implements CourseDao {

    private class FilterQueryBuilder{

        private final RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(
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

        private String SELECT = "SELECT courses.user_id, courses.subject_id," +
                "courses.description, price, professors.description, users.username," +
                "users.name, users.lastname, users.password, users.email, subjects.description," +
                "subjects.name, areas.name, areas.description, areas.area_id ";


        private String FROM = "FROM courses, professors, users, subjects, areas ";

        private String WHERE = "WHERE courses.user_id = users.user_id AND" +
                " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
                "AND areas.area_id = subjects.area_id ";

        private List<Object> params = new ArrayList<>();

        FilterQueryBuilder filterByProfessor(final Long professor_id){

            this.WHERE = this.WHERE + "AND professors.user_id = ? ";
            this.params.add(professor_id);
            return this;
        }

        FilterQueryBuilder filterByTimeslot(final Integer day, final Integer startHour, final Integer endHour){
            this.WHERE = this.WHERE + "AND schedules.day = ? AND schedules.hour >= ? AND schedules.hour < ? ";
            this.FROM = this.FROM + ",schedules ";
            this.params.add(day);
            this.params.add(startHour);
            this.params.add(endHour);
            return this;
        }

        List<Course> execute(){
            return jdbcTemplate.query(SELECT+FROM+WHERE, ROW_MAPPER, params.toArray());
        }
    }

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final String COURSES_SELECT_FROM = "SELECT courses.user_id, courses.subject_id," +
            "courses.description, price, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id " +
            "FROM courses, professors, users, subjects, areas, schedules ";
    
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
    public List<Course> findByProfessorId(final long professor_id, final int limit, final int offset) {
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id " +
                        "AND courses.subject_id = subjects.subject_id ORDER BY subjects.subject_id " +
                        " LIMIT ? OFFSET ?", ROW_MAPPER, professor_id, limit, offset
        );
        return courses;
    }

    @Override
    public List<Course> filterCoursesByName(final String name, final int limit, final int offset) {
        final String search = "%" + name + "%";
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = users.user_id AND" +
                        " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
                        "AND areas.area_id = subjects.area_id AND UPPER(subjects.name) LIKE UPPER(?)" +
                        " ORDER BY courses.user_id, courses.subject_id LIMIT ? OFFSET ?"
                , ROW_MAPPER, search, limit, offset
        );
        return courses;
    }

    @Override
    public List<Course> filterByAreaId(final long areaId, final int limit, final int offset) {
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE subjects.area_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id" +
                        " AND courses.subject_id = subjects.subject_id " +
                        " ORDER BY courses.user_id, courses.subject_id LIMIT ? OFFSET ?"
                , ROW_MAPPER, areaId, limit, offset);
        return courses;
    }

    @Override
    public List<Course> filterCoursesByTimeAndProfessor(final int day, final int startHour, final int endHour, final long professor_id) {

        FilterQueryBuilder FilterQueryBuilder = new FilterQueryBuilder();

        return FilterQueryBuilder
                .filterByTimeslot(day, startHour, endHour)
                .filterByProfessor(professor_id)
                .execute();

    }

    @Override
    public List<Course> filter(Filter filter, int limit, int offset) {
        List<Object> params = filter.getQueryParams();
        params.add(limit);
        params.add(offset);
        final List<Course> courses = jdbcTemplate.query(
                filter.getQuery() + "ORDER BY courses.user_id, courses.subject_id LIMIT ? OFFSET ?",
                ROW_MAPPER, params.toArray()
        );

        return  courses;
    }

    @Override
    public Course create(final Professor professor, final Subject subject, final String description,
                         final Double price) throws CourseAlreadyExistsException {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", professor.getId());
        args.put("subject_id", subject.getId());
        args.put("description", description);
        args.put("price", price);
        try {
            jdbcInsert.execute(args);
        } catch (DuplicateKeyException e) {
            throw new CourseAlreadyExistsException();
        }
        return new Course(professor, subject, description, price);
    }
}