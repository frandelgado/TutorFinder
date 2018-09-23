package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AreaController {

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @Autowired
    private CourseService cs;


    @RequestMapping("/Area/{id}")
    public ModelAndView area(@PathVariable(value = "id") long id){
        final ModelAndView mav = new ModelAndView("area");
        mav.addObject("area", as.findAreaById(id));
        mav.addObject("results", cs.filterByAreaId(id));
        return mav;
    }
}
