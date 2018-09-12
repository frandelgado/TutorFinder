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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
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

    private final String TEST_DESCRIPTION = "test description";

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"professors");
    }

    @Test
    public void testCreateValid(){

        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn("juan@gmail.com");
        when(mockUser.getId()).thenReturn(2l);
        when(mockUser.getLastname()).thenReturn("lopez");
        when(mockUser.getName()).thenReturn("juan");
        when(mockUser.getUsername()).thenReturn("juancho");
        when(mockUser.getPassword()).thenReturn("easypassword");

        Professor professor = professorJdbcDao.create(mockUser, TEST_DESCRIPTION);
        assertNotNull(professor);
        assertEquals(professor.getDescription(),TEST_DESCRIPTION);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "professors"));

    }


    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "professors");
    }
}
