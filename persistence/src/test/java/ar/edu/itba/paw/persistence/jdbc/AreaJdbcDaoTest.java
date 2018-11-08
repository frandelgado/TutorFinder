package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.persistence.AreaJdbcDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcTestConfig.class)
@Sql("classpath:schema.sql")
public class AreaJdbcDaoTest {

    private static final String NAME = "matematica";
    private static final String DESCRIPTION = "este area es dificil";
    private static final String INVALID_NAME = "InvalidTestName";
    private static final byte[] TEST_IMAGE = new byte[1];
    private static final Long ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private AreaJdbcDao areaDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        final Area area = areaDao.create(NAME, DESCRIPTION, TEST_IMAGE);
        assertNotNull(area);
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "areas"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testCreateInvalid() {
        final Area area = areaDao.create(NAME, DESCRIPTION, TEST_IMAGE);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "areas"));
    }

    @Test
    public void testFindByIdValid() {
        final Area area = areaDao.findById(ID);
        assertNotNull(area);
        assertEquals(ID, area.getId());
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
    }

    @Test
    public void testFindByIdInvalid() {
        final Area area = areaDao.findById(INVALID_ID);
        assertNull(area);
    }

    @Test
    public void filterAreasByNameValid() {
        final List<Area> areas = areaDao.filterAreasByName(NAME, LIMIT, OFFSET);
        assertNotNull(areas);
        assertEquals(1, areas.size());
        final Area area = areas.get(0);

        assertEquals(ID, area.getId());
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
    }

    @Test
    public void filterAreasByNameInvalid() {
        final List<Area> areas = areaDao.filterAreasByName(INVALID_NAME, LIMIT, OFFSET);
        assertNotNull(areas);
        assertEquals(0, areas.size());
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

}
