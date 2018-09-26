package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
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

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class ProfessorJdbcDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProfessorJdbcDao professorJdbcDao;

    private JdbcTemplate jdbcTemplate;


    private static final String NAME = "Juan";
    private static final String SURNAME = "lopez";
    private static final String USERNAME = "Juancho";
    private static final String EMAIL = "juancito@gmail.com";
    private static final String PASSWORD = "dontbecruel";
    private static final Long ID = 2L;
    private static final String TEST_DESCRIPTION = "Juan es un profesor dedicado";
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_USERNAME = "InvalidTestUsername";
    private static final String INVALID_FULL_NAME = "InvalidTestFullName";

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testCreateValid(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"professors");
        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn(EMAIL);
        when(mockUser.getId()).thenReturn(ID);
        when(mockUser.getLastname()).thenReturn(SURNAME);
        when(mockUser.getName()).thenReturn(NAME);
        when(mockUser.getUsername()).thenReturn(USERNAME);
        when(mockUser.getPassword()).thenReturn(PASSWORD);

        Professor professor = professorJdbcDao.create(mockUser, TEST_DESCRIPTION);
        assertNotNull(professor);
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "professors"));
    }

    @Test
    public void testFindByIdValid() {
        final Professor professor = professorJdbcDao.findById(ID).orElse(null);
        assertNotNull(professor);
        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFindByIdInvalid() {
        final Professor professor = professorJdbcDao.findById(INVALID_ID).orElse(null);
        assertNull(professor);
    }

    @Test
    public void testFindByUsernameValid() {
        final Professor professor = professorJdbcDao.findByUsername(USERNAME).orElse(null);
        assertNotNull(professor);
        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFindByUsernameInvalid() {
        final Professor professor = professorJdbcDao.findByUsername(INVALID_USERNAME).orElse(null);
        assertNull(professor);
    }

    @Test
    public void testFilterByFullNameValid() {
        final List<Professor> professors = professorJdbcDao.filterByFullName(NAME + " " + SURNAME);
        assertNotNull(professors);
        assertEquals(1,professors.size());
        final Professor professor = professors.get(0);

        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFilterByFullNameInvalid() {
        final List<Professor> professors = professorJdbcDao.filterByFullName(INVALID_FULL_NAME);
        assertNotNull(professors);
        assertEquals(0,professors.size());
    }


    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }
}
