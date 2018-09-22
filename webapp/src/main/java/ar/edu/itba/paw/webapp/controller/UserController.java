package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.RegisterProfessorForm;
import ar.edu.itba.paw.webapp.form.ScheduleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ScheduleService ss;

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final RegisterForm form,
                               final BindingResult errors, HttpServletRequest request) {
        if(errors.hasErrors() || !form.checkRepeatPassword()) {
            if(!form.checkRepeatPassword()) {
                errors.addError(new FieldError("RepeatPasswordError", "repeatPassword", form.getRepeatPassword(),
                        false, new String[]{"RepeatPassword"}, null, "Las contraseñas deben coincidir"));
            }
            return register(form);
        }
        final User u = us.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());

        authenticateRegistered(request, u.getUsername(), u.getPassword());

        final RedirectView view = new RedirectView("/" );
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/Professor/{id}")
    public ModelAndView professorProfile(@PathVariable(value = "id") long id,
                                         @ModelAttribute("currentUser") final User loggedUser,
                                         @ModelAttribute("currentUserIsProfessor") final boolean isProfessor) {
        if(loggedUser != null && loggedUser.getId() == id && isProfessor)
            return profile(loggedUser);

        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("courses", cs.findCourseByProfessorId(id));
        mav.addObject("professor", ps.findById(id));
        return mav;
    }

    @RequestMapping("/Profile")
    public ModelAndView profile(@ModelAttribute("currentUser") final User loggedUser) {
        final Professor professor = ps.findById(loggedUser.getId());

        final ModelAndView mav = new ModelAndView("profileForProfessor");

        mav.addObject("courses", cs.findCourseByProfessorId(professor.getId()));
        mav.addObject("professor", professor);

        return mav;
    }


    public void authenticateRegistered(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @RequestMapping(value = "/registerAsProfessor", method = RequestMethod.POST)
    public ModelAndView createProfessor(@Valid @ModelAttribute("registerAsProfessorForm") final RegisterProfessorForm form,
                               final BindingResult errors) {
        if(errors.hasErrors()) {
            return registerProfessor(form);
        }

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                auth.getPrincipal(),
                auth.getCredentials(),
                updatedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        final String username = newAuth.getName();
        final User user = us.findByUsername(username);
        final Professor p = ps.create(user.getId(), form.getDescription());

        final RedirectView view = new RedirectView("/" );
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping("/registerAsProfessor")
    public ModelAndView registerProfessor(@ModelAttribute("registerAsProfessorForm") final RegisterProfessorForm form) {
        return new ModelAndView("registerAsProfessorForm");
    }

    @RequestMapping(value = "/CreateTimeSlot", method = RequestMethod.POST)
    public ModelAndView createTimeslot(
            @Valid @ModelAttribute("newScheduleForm") final ScheduleForm form,
            final BindingResult errors
    ){
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.addError(new FieldError("profile.add_schedule.timeError", "time", form.getEndHour(),
                false, new String[]{"profile.add_schedule.timeError"}, null, "El horario de comienzo debe ser menor al de finalización"));
            }
            return profile(form);
        }

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();

        final Professor professor = ps.findByUsername(username);

        try {
            ss.reserveTimeSlot(professor.getId(), form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (InvalidTimeException e) {
            //TODO: alguna de las horas no tienen sentido (numeros negativos o mayores a su tope) . Redirigir.
        } catch (InvalidTimeRangeException e) {
            //TODO: startHour es mas grande que endHour, manejar el error
        }
        return profile(form);
    }
}
