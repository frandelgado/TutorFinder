package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.RegisterProfessorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

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
        final User u = us.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());
        return new ModelAndView("redirect:/?userId="+ u.getId());
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/Professor/{id}")
    public ModelAndView professorProfile(@PathVariable(value = "id") long id) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("courses", cs.findCourseByProfessorId(id));
        mav.addObject("professor", ps.findById(id));
        return mav;
    }

    @RequestMapping("/Profile")
    public ModelAndView profile() {
        final ModelAndView mav = new ModelAndView("profileForProfessor");

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();

        final Professor professor = ps.findByUsername(username);

        mav.addObject("courses", cs.findCourseByProfessorId(professor.getId()));
        mav.addObject("professor", professor);
        return mav;
    }
}
