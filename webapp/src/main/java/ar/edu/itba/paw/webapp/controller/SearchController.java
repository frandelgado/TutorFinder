package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    //TODO: MAYBE HAVING A SEARCH SERVICE WOULD BE A GOOD IDEA
    @RequestMapping("/searchResults")
    public ModelAndView search(@RequestParam(value = "search") final String name,
                               @RequestParam(value = "type", defaultValue = "course") final String type,
                               @RequestParam(value = "page", defaultValue = "1") final int page,
                               @RequestParam(value = "day", required = false)final Integer day,
                               @RequestParam(value = "startHour", required = false)final Integer startHour,
                               @RequestParam(value = "endHour", required = false)final Integer endHour,
                               @RequestParam(value = "minPrice", required = false)final Double minPrice,
                               @RequestParam(value = "maxPrice", required = false) final Double maxPrice) throws PageOutOfBoundsException {
        final ModelAndView mav = new ModelAndView("searchResults");
        mav.addObject("page", page);

        switch (type) {
            case "professor":
                mav.addObject("pagedResults", ps.filterByFullName(name, page));
                break;
            case "course":
                mav.addObject("pagedResults", cs.filterCourses(day,startHour, endHour,minPrice, maxPrice, name, page));
                break;
            case "area":
                mav.addObject("pagedResults", as.filterAreasByName(name, page));
                break;
            default:
                final ModelAndView error = new ModelAndView("error");
                error.addObject("errorMessageCode","typeInvalid");
                return error;
        }
        mav.addObject("search", name);
        mav.addObject("type", type);
        return mav;
    }

}
