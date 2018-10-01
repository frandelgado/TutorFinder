package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AreaController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @Autowired
    private CourseService cs;


    @RequestMapping("/Area/{id}")
    public ModelAndView area(@PathVariable(value = "id") final long id,
                             @RequestParam(value = "page", defaultValue = "1") final int page) throws PageOutOfBoundsException {
        final ModelAndView mav = new ModelAndView("area");
        final Area area = as.findAreaById(id);
        if(area == null) {
            return redirectToErrorPage("nonExistentArea");
        }

        LOGGER.info("Creating view for Area with id {}", id);
        mav.addObject("area", area);
        mav.addObject("pagedResults", cs.filterByAreaId(id, page));
        mav.addObject("page", page);
        return mav;
    }
}
