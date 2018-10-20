package ar.edu.itba.paw.persistence.Hibernate;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserHibernateDao;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
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

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHibernateDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Test
    public void stub(){

    }

}
