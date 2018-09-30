package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PasswordResetToken;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @Autowired
    private PasswordResetService passwordResetService;

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final RegisterForm form,
                               final BindingResult errors, HttpServletRequest request) {
        if(errors.hasErrors() || !form.checkRepeatPassword()) {
            if(!form.checkRepeatPassword()) {
                errors.rejectValue("repeatPassword", "RepeatPassword");
            }
            return register(form);
        }
        final User u;

        try {
            u = us.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());
        } catch (EmailAlreadyInUseException e) {
            errors.rejectValue("email", "RepeatedEmail");
            return register(form);
        } catch (UsernameAlreadyInUseException e) {
            errors.rejectValue("username", "RepeatedUsername");
            return register(form);
        }

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
                                         @ModelAttribute("currentUserIsProfessor") final boolean isProfessor,
                                         @RequestParam(value = "page", defaultValue = "1") final int page) throws PageOutOfBoundsException {
        if(loggedUser != null && loggedUser.getId() == id && isProfessor) {
            final RedirectView view = new RedirectView("/Profile");
            view.setExposeModelAttributes(false);
            return new ModelAndView(view);
        }

        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("courses", cs.findCourseByProfessorId(id, page));
        mav.addObject("page", page);
        final Professor professor = ps.findById(id);
        if(professor == null) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","nonExistentProfessor");
            return error;
        }
        mav.addObject("professor", professor);
        return mav;
    }

    //TODO: CHECK
    @RequestMapping("/Profile")
    public ModelAndView profile(
            @ModelAttribute("currentUser") final User loggedUser,
            @ModelAttribute("ScheduleForm") final ScheduleForm scheduleForm,
            @RequestParam(value = "page", defaultValue = "1") final int page
    ) throws PageOutOfBoundsException, NonexistentProfessorException {
        final Professor professor = ps.findById(loggedUser.getId());
        if(professor == null) {
            throw new NonexistentProfessorException();
        }

        final ModelAndView mav = new ModelAndView("profileForProfessor");

        final Schedule schedule = ss.getScheduleForProfessor(professor.getId());

        mav.addObject("courses", cs.findCourseByProfessorId(professor.getId(), page));
        mav.addObject("professor", professor);
        mav.addObject("schedule", schedule);
        mav.addObject("page", page);
        return mav;
    }
    
    private void authenticateRegistered(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @RequestMapping(value = "/registerAsProfessor", method = RequestMethod.POST)
    public ModelAndView createProfessor(@ModelAttribute("currentUser") final User loggedUser,
                                        @Valid @ModelAttribute("registerAsProfessorForm") final RegisterProfessorForm form,
                                        final BindingResult errors, final HttpServletRequest request) throws ProfessorWithoutUserException {
        if(errors.hasErrors()) {
            return registerProfessor(form);
        }

        final Professor p = ps.create(loggedUser.getId(), form.getDescription());

        authenticateRegistered(request, p.getUsername(), p.getPassword());

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
            @ModelAttribute("currentUser") final User loggedUser,
            @Valid @ModelAttribute("ScheduleForm") final ScheduleForm form,
            final BindingResult errors
    ) throws PageOutOfBoundsException, NonexistentProfessorException {
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            return profile(loggedUser, form, 1);
        }

        try {
            ss.reserveTimeSlot(loggedUser.getId(), form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (NonexistentProfessorException e) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","nonExistentUser");
            return error;
        } catch (TimeslotAllocatedException e) {
            errors.rejectValue("endHour", "TimeslotAllocatedError");
            return profile(loggedUser, form, 1);
        } catch (InvalidTimeException | InvalidTimeRangeException e) {
            //Already validated by form
            return profile(loggedUser, form, 1);
        }

        final RedirectView view = new RedirectView("/Profile" );
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/forgotPassword")
    public ModelAndView forgotPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordRequestForm form) {
        return new ModelAndView("forgotPassword");
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ModelAndView forgotPassword(@Valid @ModelAttribute("resetPasswordForm") final ResetPasswordRequestForm form,
                                       final BindingResult errors) {
        if(errors.hasErrors()) {
            return forgotPassword(form);
        }

        final boolean created = passwordResetService.createToken(form.getEmail());

        if(!created) {
            errors.rejectValue("email", "forgotPasswordError");
            return forgotPassword(form);
        }
        errors.rejectValue("successMessage", "forgotPasswordSuccess");
        form.setEmail("");
        return forgotPassword(form);
    }

    @RequestMapping("/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordForm form,
                                      @RequestParam(value="token", required=true) final String token) {

        if(token.isEmpty()) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","invalidToken");
            return error;
        }

        final PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if(passwordResetToken == null) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","invalidToken");
            return error;
        }

        return new ModelAndView("resetPassword");
    }


    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordForm form,
                                      final BindingResult errors,
                                      @RequestParam(value="token", required=true) final String token,
                                      HttpServletRequest request) {
        if(errors.hasErrors() || !form.checkRepeatPassword()) {
            if(!form.checkRepeatPassword()) {
                errors.rejectValue("repeatPassword", "RepeatPassword");
            }
            return resetPassword(form, token);
        }

        final User changedUser;
        try {
            changedUser = passwordResetService.changePassword(token, form.getPassword());
        } catch (InvalidTokenException e) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","invalidToken");
            return error;
        }

        if(changedUser == null) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","changePasswordError");
            return error;
        }

        authenticateRegistered(request, changedUser.getUsername(), form.getPassword());
        final RedirectView view = new RedirectView("/" );
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }


}
