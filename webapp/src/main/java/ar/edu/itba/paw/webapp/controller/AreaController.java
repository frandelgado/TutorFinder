package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AreaController {

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;


    @RequestMapping("/Area/{id}")
    public ModelAndView area(@PathVariable(value = "id") long id){
        final ModelAndView mav = new ModelAndView("area");
        mav.addObject("area", as.findAreaById(id));
        return mav;
    }


    @RequestMapping("/createArea")
    public ModelAndView create(
            @RequestParam(value="name", required=true) final String name,
            @RequestParam(value="description", required=true) final String description
    )
    {
        final Area a = as.create(name, description);
        return new ModelAndView("redirect:/Area/" + a.getId());
    }
}
