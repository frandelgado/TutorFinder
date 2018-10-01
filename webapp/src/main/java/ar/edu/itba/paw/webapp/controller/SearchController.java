package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
import javax.validation.Valid;

@Controller
public class SearchController extends BaseController{

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @RequestMapping(value = "/searchResults", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(value = "page", defaultValue = "1") final int page,
                               @ModelAttribute("searchForm") final SearchForm form) throws PageOutOfBoundsException {

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
                return redirectToErrorPage("typeInvalid");
        }
        mav.addObject("search", form.getSearch());
        mav.addObject("type", form.getType());
        return mav;
    }

    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public ModelAndView search(@RequestParam(value = "page", defaultValue = "1")final int page,
                               @Valid @ModelAttribute("searchForm") final SearchForm form,
                               final BindingResult errors,
                               final RedirectAttributes redirectAttributes) throws PageOutOfBoundsException {

        if(errors.hasErrors() || !form.validPriceRange() || !form.validTimeRange()) {
            if(!form.validPriceRange()){
                errors.rejectValue("maxPrice", "invalidPriceRange");
            }
            if(!form.validTimeRange()){
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.searchForm", errors);
            redirectAttributes.addFlashAttribute("searchForm", form);
        }else {
            redirectAttributes.addFlashAttribute("searchForm", form);
            redirectAttributes.addFlashAttribute("page", page);
        }
        RedirectView redirect = new RedirectView("/searchResults");
        return new ModelAndView(redirect);


    }

}
