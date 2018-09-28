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
import org.springframework.validation.FieldError;
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
                errors.addError(new FieldError("RepeatPasswordError", "repeatPassword", form.getRepeatPassword(),
                        false, new String[]{"RepeatPassword"}, null, "Las contraseñas deben coincidir"));
            }
            return register(form);
        }
        final User u;

        try {
            u = us.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());
        } catch (EmailAlreadyInUseException e) {
            errors.addError(new FieldError("RepeatedEmail", "email", form.getEmail(),
                    false, new String[]{"RepeatedEmail"}, null, "El correo electronico ya esta en uso"));
            return register(form);
        } catch (UsernameAlreadyInUseException e) {
            errors.addError(new FieldError("RepeatedUsername", "username", form.getUsername(),
                    false, new String[]{"RepeatedUsername"}, null, "El nombre de usuario ya esta en uso"));
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

    @RequestMapping("/Profile")
    public ModelAndView profile(
            @ModelAttribute("currentUser") final User loggedUser,
            @ModelAttribute("ScheduleForm") final ScheduleForm scheduleForm,
            @RequestParam(value = "page", defaultValue = "1") final int page
    ) throws PageOutOfBoundsException {
        final Professor professor = ps.findById(loggedUser.getId());

        final ModelAndView mav = new ModelAndView("profileForProfessor");

        Schedule schedule = ss.getScheduleForProfessor(professor);

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
                                        final BindingResult errors, final HttpServletRequest request) {
        if(errors.hasErrors()) {
            return registerProfessor(form);
        }

        final User user = us.findUserById(loggedUser.getId());

        final Professor p;
        try {
            p = ps.create(user.getId(), form.getDescription());
        } catch (ProfessorWithoutUserException e) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","nonExistentUser");
            return error;
        }

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
    ) throws PageOutOfBoundsException {
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.addError(new FieldError("profile.add_schedule.timeError", "endHour", form.getEndHour(),
                false, new String[]{"profile.add_schedule.timeError"}, null, "El horario de comienzo debe ser menor al de finalización"));
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
            errors.addError(new FieldError("TimeslotAllocatedError", "endHour", form.getEndHour(),
                    false, new String[]{"TimeslotAllocatedError"}, null, "El horario ya fue seleccionado previamente"));
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
            errors.addError(new FieldError("forgotPasswordError", "email", form.getEmail(),
                    false, new String[]{"forgotPasswordError"}, null, "El correo electronico no se encuentra registrado"));
            return forgotPassword(form);
        }
        errors.addError(new FieldError("forgotPasswordSuccess", "successMessage", null,
                false, new String[]{"forgotPasswordSuccess"}, null,
                "Se envió un correo electronico a su cuenta, siga los pasos para restaurar su contraseña"));
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
                errors.addError(new FieldError("RepeatPasswordError", "repeatPassword", form.getRepeatPassword(),
                        false, new String[]{"RepeatPassword"}, null, "Las contraseñas deben coincidir"));
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
