package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.models.Area;
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
public class AreaJdbcDao implements AreaDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Area> ROW_MAPPER = (rs, rowNum) -> new Area(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3)
    );

    @Autowired
    public AreaJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("areas")
                .usingGeneratedKeyColumns("area_id");
    }
    @Override
    public Optional<Area> findById(final long id) {
        final List<Area> list = jdbcTemplate.query(
                "SELECT area_id, description, name FROM areas WHERE area_id = ?", ROW_MAPPER, id
        );
        return list.stream().findFirst();
    }

    @Override
    public Area create(final String name, final String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("description", description);
        final Number areaId = jdbcInsert.executeAndReturnKey(args);
        return new Area(areaId.longValue(), description, name);
    }

    @Override
    public List<Area> filterAreasByName(final String name) {
        final String search = "%" + name + "%";
        final List<Area> list = jdbcTemplate.query(
                "SELECT area_id, description, name FROM areas WHERE UPPER(name) LIKE UPPER(?)",
                ROW_MAPPER, search
        );
        return list;
    }

    @Override
    public List<Area> filterAreasByName(final String name, final int limit, final int offset) {
        final String search = "%" + name + "%";
        final List<Area> list = jdbcTemplate.query(
                "SELECT area_id, description, name FROM areas WHERE UPPER(name) LIKE UPPER(?) " +
                        "order by area_id LIMIT ? OFFSET ?",
                ROW_MAPPER, search, limit, offset
        );
        return list;
    }
}