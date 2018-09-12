package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final RegisterForm form,
                               final BindingResult errors) {
        if(errors.hasErrors()) {
            return register(form);
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

}
