package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
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

    @RequestMapping("/searchResults")
    public ModelAndView search(@RequestParam(value = "search") String name,
                               @RequestParam(value = "type", defaultValue = "course") String type){
        final ModelAndView mav = new ModelAndView("searchResults");

        switch (type) {
            case "professor":
                mav.addObject("results", ps.filterByFullName(name));
                break;
            case "course":
                mav.addObject("results", cs.filterCoursesByName(name));
                break;
            case "area":
                mav.addObject("results", as.filterAreasByName(name));
                break;
            default:
                final ModelAndView error = new ModelAndView("error");
                error.addObject("errorMessageCode","typeInvalid");
                return error;
        }
        mav.addObject("search", name.isEmpty()? " ": name);
        mav.addObject("type", type);
        return mav;
    }

}
