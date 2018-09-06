package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
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
public class SubjectJdbcDaoTest {

    private static final String NAME = "TestName";
    private static final String DESCRIPTION = "TestDescription";
    private static final Long AREA_ID = 1l;

    @Autowired
    private DataSource ds;

    @Autowired
    private SubjectJdbcDao subjectDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");


    }

    @Test
    public void testCreate() {
        final Subject subject = subjectDao.create(NAME, DESCRIPTION, AREA_ID);
        assertNotNull(subject);
        assertEquals(NAME, subject.getName());
        assertEquals(DESCRIPTION, subject.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
    }
}
