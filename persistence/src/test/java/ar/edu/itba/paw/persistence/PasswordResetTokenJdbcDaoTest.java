package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.junit.After;
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

import java.sql.Timestamp;
import java.util.UUID;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class PasswordResetTokenJdbcDaoTest {

    private static final String RANDOM_UUID = UUID.randomUUID().toString();
    private static final String TOKEN = "123e4567-e89b-12d3-a456-556642440000";
    private static final Long ID = 1L;
    private static final Long USER_ID = 5L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_TOKEN = "123e4567-e89b-12d3-a456-666666666666";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordResetTokenJdbcDao passwordResetTokenJdbcDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testCreateValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"reset_password_tokens");
        final PasswordResetToken token = passwordResetTokenJdbcDao.create(USER_ID, RANDOM_UUID,
                LocalDateTime.now().plusDays(1));

        assertNotNull(token);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }

    @Test
    public void testCreateInvalid() {
        final PasswordResetToken token = passwordResetTokenJdbcDao.create(INVALID_ID, RANDOM_UUID,
                LocalDateTime.now().plusDays(1));
        assertNull(token);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }


    @Test
    public void testFindByTokenValid() {
        final PasswordResetToken token = passwordResetTokenJdbcDao.findByToken(TOKEN);
        assertNotNull(token);
        assertEquals(ID, token.getId());
        assertEquals(USER_ID, token.getUser().getId());
        assertEquals(TOKEN, token.getToken());
    }

    @Test
    public void testFindByTokenInvalid() {
        final PasswordResetToken token = passwordResetTokenJdbcDao.findByToken(INVALID_TOKEN);
        assertNull(token);
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
    }


}
