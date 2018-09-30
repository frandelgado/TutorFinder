package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.PagedResults;
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

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AreaServiceImplTest {

    private static final String NAME = "Mate";
    private static final int PAGE_SIZE = 3;


    @InjectMocks
    @Autowired
    private AreaService areaService;

    @Mock
    private AreaDao areaDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterAreasByNameHasNext() throws PageOutOfBoundsException {
        final List<Area> areas = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE + 1; i++) {
            areas.add(mock(Area.class));
        }
        when(areaDao.filterAreasByName(eq(NAME), anyInt(), anyInt())).thenReturn(areas);

        final PagedResults<Area> results = areaService.filterAreasByName(NAME, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterAreasByNameNoNext() throws PageOutOfBoundsException {
        final List<Area> areas = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            areas.add(mock(Area.class));
        }
        when(areaDao.filterAreasByName(eq(NAME), anyInt(), anyInt())).thenReturn(areas);

        final PagedResults<Area> results = areaService.filterAreasByName(NAME, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterAreasByNamePageOutOfBounds() throws PageOutOfBoundsException {
        final List<Area> areas = mock(List.class);
        when(areas.size()).thenReturn(0);
        when(areaDao.filterAreasByName(eq(NAME), anyInt(), anyInt())).thenReturn(areas);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Area> results = areaService.filterAreasByName(NAME, INVALID_PAGE);
    }

    @Test(expected = PageOutOfBoundsException.class)
    public void testFilterAreasByNameNegativePage() throws PageOutOfBoundsException {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Area> results = areaService.filterAreasByName(NAME, INVALID_PAGE);
    }
}
