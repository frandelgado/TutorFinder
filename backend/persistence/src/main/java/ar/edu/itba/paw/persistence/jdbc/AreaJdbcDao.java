package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.models.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaJdbcDao implements AreaDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaJdbcDao.class);

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Area> ROW_MAPPER = (rs, rowNum) -> new Area(
            rs.getString(2),
            rs.getString(3),
            rs.getBytes(4)
    );

    @Autowired
    public AreaJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("areas")
                .usingGeneratedKeyColumns("area_id");
    }
    @Override
    public Area findById(final long id) {
        LOGGER.trace("Querying for area with id {}", id);
        final List<Area> list = jdbcTemplate.query(
                "SELECT area_id, description, name, image FROM areas WHERE area_id = ?", ROW_MAPPER, id
        );
        return list.stream().findFirst().get();
    }

    @Override
    public Area create(final String name, final String description, final byte[] image) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("description", description);
        args.put("image", image);
        final Number areaId = jdbcInsert.executeAndReturnKey(args);
        return new Area(description, name, image);
    }

    @Override
    public List<Area> filterAreasByName(final String name, final int limit, final int offset) {
        final String search = "%" + name + "%";
        LOGGER.trace("Querying for areas with name containing {}", name);
        final List<Area> list = jdbcTemplate.query(
                "SELECT area_id, description, name, image FROM areas WHERE UPPER(name) LIKE UPPER(?) " +
                        "order by area_id LIMIT ? OFFSET ?",
                ROW_MAPPER, search, limit, offset
        );
        return list;
    }

    @Override
    public long totalAreasByName(String name) {
        return 0;
    }
}