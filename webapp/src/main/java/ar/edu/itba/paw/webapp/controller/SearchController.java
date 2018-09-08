package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    //TODO: Search with dropdown, for now just searches areas
    @RequestMapping("/searchResults")
    public ModelAndView search(@RequestParam(value = "search") String name){
        final ModelAndView mav = new ModelAndView("searchResults");
        mav.addObject("results", as.filterAreasByName(name));
        mav.addObject("search", name);
        return mav;
    }

}
