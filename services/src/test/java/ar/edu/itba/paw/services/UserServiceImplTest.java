package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceImplTest {

    private static final String NAME = "Marcos";
    private static final int PAGE_SIZE = 5;
    private static final Long INVALID_ID = 666L;
    private static final Long ID = 1L;
    private static final String USERNAME = "username";
    private static final String LASTNAME = "Ramos";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "carlitos@gmail.com";


    @InjectMocks
    @Autowired
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateValid() throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userDao.create(USERNAME, encoder.encode(PASSWORD), EMAIL, NAME, LASTNAME)).thenReturn(mock(User.class));

        final User user = userService.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME);
        assertNotNull(user);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void testCreateWithUserEmailInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userDao.create(USERNAME, encoder.encode(PASSWORD), EMAIL, NAME, LASTNAME)).thenThrow(new EmailAlreadyInUseException());

        final User user = userService.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void testCreateWithUserUsernameInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userDao.create(USERNAME, encoder.encode(PASSWORD), EMAIL, NAME, LASTNAME)).thenThrow(new UsernameAlreadyInUseException());

        final User user = userService.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME);
    }
}
