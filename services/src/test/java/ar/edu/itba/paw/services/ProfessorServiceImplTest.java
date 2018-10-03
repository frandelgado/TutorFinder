package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProfessorServiceImplTest {

    private static final String NAME = "Marcos";
    private static final int PAGE_SIZE = 3;
    private static final Long INVALID_ID = 666L;
    private static final Long ID = 1L;
    private static final String DESCRIPTION = "Omne duo vim sum nudi uno quod. Latera nullam ad realem passim ii essent ut patere";
    private static final byte[] TEST_IMAGE = new byte[1];
    private static final String USERNAME = "username";
    private static final String LASTNAME = "Ramos";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "carlitos@gmail.com";


    @InjectMocks
    @Autowired
    private ProfessorService professorService;

    @Mock
    private ProfessorDao professorDao;

    @Mock
    private UserDao userDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterByFullNameHasNext() {
        final List<Professor> professors = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            professors.add(mock(Professor.class));
        }
        when(professorDao.filterByFullName(eq(NAME), anyInt(), anyInt())).thenReturn(professors);

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterByFullNameNoNext() {
        final List<Professor> professors = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            professors.add(mock(Professor.class));
        }
        when(professorDao.filterByFullName(eq(NAME), anyInt(), anyInt())).thenReturn(professors);

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test
    public void testFilterByFullNamePageOutOfBounds() {
        final List<Professor> professors = mock(List.class);
        when(professors.size()).thenReturn(0);
        when(professorDao.filterByFullName(eq(NAME), anyInt(), anyInt())).thenReturn(professors);

        final Integer INVALID_PAGE = 666;

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, INVALID_PAGE);
        assertNull(results);
    }

    @Test
    public void testFilterByFullNameNegativePage() {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, INVALID_PAGE);
        assertNull(results);
    }

    @Test
    public void testCreateValid() throws ProfessorWithoutUserException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(ID);
        when(userDao.findById(ID)).thenReturn(Optional.of(user));
        when(professorDao.create(user, DESCRIPTION, TEST_IMAGE)).thenReturn(mock(Professor.class));

        final Professor professor = professorService.create(ID, DESCRIPTION, TEST_IMAGE);
        assertNotNull(professor);
    }

    @Test(expected = ProfessorWithoutUserException.class)
    public void testCreateInvalidUser() throws ProfessorWithoutUserException {
        when(userDao.findById(INVALID_ID)).thenReturn(Optional.empty());

        final Professor professor = professorService.create(INVALID_ID, DESCRIPTION, TEST_IMAGE);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void testCreateWithUserEmailInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenThrow(new EmailAlreadyInUseException());

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION, TEST_IMAGE);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void testCreateWithUserUsernameInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenThrow(new UsernameAlreadyInUseException());

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION, TEST_IMAGE);
    }

    @Test
    public void testCreateWithUserInvalidDescription() throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final String description = "Short";

        Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, description, TEST_IMAGE);
        assertNull(professor);
    }

    @Test
    public void testCreateWithUserValid() throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final User user = new User(ID, USERNAME, NAME, LASTNAME, PASSWORD, EMAIL);
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenReturn(user);
        when(professorDao.create(user, DESCRIPTION, TEST_IMAGE)).thenReturn(mock(Professor.class));

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION, TEST_IMAGE);

        assertNotNull(professor);
    }
}
