package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
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
    private static final int PAGE_SIZE = 5;
    private static final Long INVALID_ID = 666L;
    private static final Long ID = 1L;
    private static final String DESCRIPTION = "Omne duo vim sum nudi uno quod. Latera nullam ad realem passim ii essent ut patere";
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
    public void testFilterByFullNameHasNext() throws PageOutOfBoundsException {
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
    public void testFilterByFullNameNoNext() throws PageOutOfBoundsException {
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

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterByFullNamePageOutOfBounds() throws PageOutOfBoundsException {
        final List<Professor> professors = mock(List.class);
        when(professors.size()).thenReturn(0);
        when(professorDao.filterByFullName(eq(NAME), anyInt(), anyInt())).thenReturn(professors);

        final Integer INVALID_PAGE = 666;

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterByFullNameNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Professor> results = professorService.filterByFullName(NAME, INVALID_PAGE);
    }

    @Test
    public void testCreateValid() throws ProfessorWithoutUserException {
        final User user = mock(User.class);
        when(user.getId()).thenReturn(ID);
        when(userDao.findById(ID)).thenReturn(Optional.of(user));
        when(professorDao.create(user, DESCRIPTION)).thenReturn(mock(Professor.class));

        final Professor professor = professorService.create(ID, DESCRIPTION);
        assertNotNull(professor);
    }

    @Test(expected = ProfessorWithoutUserException.class)
    public void testCreateInvalidUser() throws ProfessorWithoutUserException {
        when(userDao.findById(INVALID_ID)).thenReturn(Optional.empty());

        final Professor professor = professorService.create(INVALID_ID, DESCRIPTION);
    }

    @Test(expected = EmailAlreadyInUseException.class)
    public void testCreateWithUserEmailInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenThrow(new EmailAlreadyInUseException());

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION);
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void testCreateWithUserUsernameInUse() throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenThrow(new UsernameAlreadyInUseException());

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION);
    }

    @Test
    public void testCreateWithUserInvalidDescription() throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        final String description = "Short";

        Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, description);
        assertNull(professor);
    }

    @Test
    public void testCreateWithUserValid() throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        final User user = new User(ID, USERNAME, NAME, LASTNAME, PASSWORD, EMAIL);
        when(userDao.create(USERNAME, PASSWORD, EMAIL, NAME, LASTNAME)).thenReturn(user);
        when(professorDao.create(user, DESCRIPTION)).thenReturn(mock(Professor.class));

        final Professor professor = professorService.createWithUser(ID, USERNAME, NAME, LASTNAME, PASSWORD,
                EMAIL, DESCRIPTION);

        assertNotNull(professor);
    }
}
