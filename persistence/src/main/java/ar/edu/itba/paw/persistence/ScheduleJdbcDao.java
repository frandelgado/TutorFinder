package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJdbcDao.class);

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
            LOGGER.trace("Inserting timeslot for professor with id {}, day {} at hour {}", professor.getId(),
                    day, hour);
            jdbcInsert.execute(args);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Timeslot for professor with id {} at day {}, hour {} already exists", professor.getId(),
                    day, hour );
            throw new TimeslotAllocatedException();
        }
        return new Timeslot(day, hour);

    }

    @Override
    public List<Timeslot> getTimeslotsForProfessor(Professor professor) {
        LOGGER.trace("Querying for timeslots from professor with id {}", professor.getId());
        List<Timeslot> schedule = jdbcTemplate.query("SELECT schedules.day, schedules.hour FROM schedules WHERE user_id = ?", TIMESLOT_ROW_MAPPER, professor.getId());
        return schedule;
    }
}
