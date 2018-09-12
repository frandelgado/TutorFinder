package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserJdbcDaoTest {

    private static final String NAME = "Juan";
    private static final String SURNAME = "lopez";
    private static final String USERNAME = "Juancho";
    private static final String EMAIL = "juancito@gmail.com";
    private static final String PASSWORD = "dontbecruel";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
    }

    @Test
    public void testCreateValid(){

        final User user = userDao.create(USERNAME, PASSWORD, EMAIL, NAME, SURNAME);
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }


}
