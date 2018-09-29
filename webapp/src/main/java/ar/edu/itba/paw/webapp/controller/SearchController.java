package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
import javax.validation.Valid;

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
    public ModelAndView search(@RequestParam(value = "page", defaultValue = "1") final int page,
                               @ModelAttribute("searchForm") final SearchForm form,
                               final BindingResult errors) throws PageOutOfBoundsException {
        if(errors.hasErrors()) {
            RedirectView redirectView = new RedirectView("/");
            return new ModelAndView(redirectView);
        }

        final ModelAndView mav = new ModelAndView("searchResults");
        mav.addObject("page", page);

        switch (form.getType()) {
            case "professor":
                mav.addObject("pagedResults", ps.filterByFullName(form.getSearch(), page));
                break;
            case "course":
                mav.addObject("pagedResults", cs.filterCourses(
                        form.getDay(), form.getStartHour(), form.getEndHour(),
                        form.getMinPrice(), form.getMaxPrice(), form.getSearch(), page
                ));
                break;
            case "area":
                mav.addObject("pagedResults", as.filterAreasByName(form.getSearch(), page));
                break;
            default:
                final ModelAndView error = new ModelAndView("error");
                error.addObject("errorMessageCode","typeInvalid");
                return error;
        }
        mav.addObject("search", form.getSearch());
        mav.addObject("type", form.getType());
        return mav;
    }

}
