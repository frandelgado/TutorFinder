package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @Autowired
    @Qualifier("subjectServiceImpl")
    private SubjectService ss;

    @RequestMapping("/")
    public ModelAndView helloWorld(){
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findUserById(0));
        return mav;
    }
    @RequestMapping("/Subject/{id}")
    public ModelAndView subject(@PathVariable(value = "id") long id){
        final ModelAndView mav = new ModelAndView("subject");
        mav.addObject("subject", ss.findSubjectById(id));
        return mav;
    }

    @RequestMapping("/createSubject")
    public ModelAndView create(
            @RequestParam(value="name", required=true) final String name,
            @RequestParam(value="description", required=true) final String description
        )
    {
        final Subject s = ss.create(name, description);
        return new ModelAndView("redirect:/Subject/" + s.getId());
    }

}
