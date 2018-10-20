package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.User;
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

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class UserHibernateDaoTest {

    private static final String NAME = "Juan";
    private static final String SURNAME = "lopez";
    private static final String USERNAME = "Juancho";
    private static final String EMAIL = "juancito@gmail.com";
    private static final String PASSWORD = "dontbecruel";
    private static final String NEW_PASSWORD = "becruel";
    private static final Long ID = 2L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_USERNAME = "InvalidTestUsername";
    private static final String INVALID_EMAIL = "InvalidTestEmail";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserHibernateDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testCreateValid() throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"users");
        final User user = userDao.create(USERNAME, PASSWORD, EMAIL, NAME, SURNAME);
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void testCreateRepeatUsername() throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final String otherEmail = "OtherEmailTest";
        final User user = userDao.create(USERNAME, PASSWORD, otherEmail, NAME, SURNAME);
        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void testCreateRepeatEmail() throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final String otherUsername = "OtherUsernameTest";
        final User user = userDao.create(otherUsername, PASSWORD, EMAIL, NAME, SURNAME);
        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test(expected = UsernameAndEmailAlreadyInUseException.class)
    public void testCreateRepeatEmailAndPassword() throws EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException, UsernameAlreadyInUseException {
        final User user = userDao.create(USERNAME, PASSWORD, EMAIL, NAME, SURNAME);
        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testFindByIdValid() {
        final User user = userDao.findById(ID).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByIdInvalid() {
        final User user = userDao.findById(INVALID_ID).orElse(null);
        assertNull(user);
    }

    @Test
    public void testFindByUsernameValid() {
        final User user = userDao.findByUsername(USERNAME).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByUsernameInvalid() {
        final User user = userDao.findByUsername(INVALID_USERNAME).orElse(null);
        assertNull(user);
    }

    @Test
    public void testFindByEmailValid() {
        final User user = userDao.findByEmail(EMAIL).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByEmailInvalid() {
        final User user = userDao.findByEmail(INVALID_EMAIL).orElse(null);
        assertNull(user);
    }

    @Test
    public void testChangePasswordByIdValid() {
        final boolean changed = userDao.changePasswordById(ID, NEW_PASSWORD);
        assertTrue(changed);
    }

    @Test
    public void testChangePasswordByIdInvalid() {
        final boolean changed = userDao.changePasswordById(INVALID_ID, NEW_PASSWORD);
        assertFalse(changed);
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
    }

}
