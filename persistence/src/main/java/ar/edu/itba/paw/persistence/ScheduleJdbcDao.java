package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
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

@Repository
public class ScheduleJdbcDao implements ScheduleDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Timeslot> TIMESLOT_ROW_MAPPER =
            (rs, rowNum) -> new Timeslot(rs.getInt(1), rs.getInt(2));

    @Autowired
    public ScheduleJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedules");
    }

    @Override
    public Timeslot reserveTimeSlot(Professor professor, Integer day, Integer hour) throws TimeslotAllocatedException {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", professor.getId());
        args.put("day", day);
        args.put("hour", hour);
        try {
            jdbcInsert.execute(args);
        } catch (DuplicateKeyException e) {
            throw new TimeslotAllocatedException();
        }
        return new Timeslot(day, hour);

    }

    @Override
    public List<Timeslot> getTimeslotsForProfessor(Professor professor) {
        List<Timeslot> schedule = jdbcTemplate.query("SELECT schedules.day, schedules.hour FROM schedules WHERE user_id = ?", TIMESLOT_ROW_MAPPER, professor.getId());
        return schedule;
    }
}
