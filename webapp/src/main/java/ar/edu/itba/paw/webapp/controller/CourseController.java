package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;


@Controller
public class CourseController {

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService courseService;

    @Autowired
    @Qualifier("subjectServiceImpl")
    private SubjectService subjectService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @ModelAttribute("messageForm") final MessageForm form,
            @RequestParam(value="professor", required=true) final Long professorId,
            @RequestParam(value="subject", required=true) final Long subjectId,
            @RequestParam(value = "SUCCESS_MESSAGE", required = false) final String success_message,
            @RequestParam(value = "ERROR_MESSAGE", required = false) final String errror_message
    ){
        final ModelAndView mav = new ModelAndView("course");
        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","nonExistentCourse");
            return error;
        }
        mav.addObject("course", course);
        mav.addObject("SUCCESS_MESSAGE", success_message);
        mav.addObject("ERROR_MESSAGE", errror_message);

        final Schedule schedule = scheduleService.getScheduleForProfessor(professorId);
        mav.addObject("schedule", schedule);
        form.setProfessorId(professorId);
        form.setSubjectId(subjectId);
        return mav;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ModelAndView contact(
            @Valid @ModelAttribute("messageForm") final MessageForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) throws UserNotInConversationException, NonexistentConversationException {

        if(errors.hasErrors()) {
            return course(form, form.getProfessorId(), form.getSubjectId(), null, null);
        }

        final boolean sent;
        try {
            sent = conversationService.sendMessage(loggedUser.getId(), form.getProfessorId(),
                    form.getSubjectId(), form.getBody());
        } catch (SameUserConversationException e) {
            errors.addError(new FieldError("SendMessageError", "extraMessage", null,
                    false, new String[]{"SameUserMessageError"},null, "No puede enviarse un mensaje a si mismo"));
            form.setBody(null);
            return course(form, form.getProfessorId(), form.getSubjectId(), null, null);
        }
        if(sent) {
            errors.addError(new FieldError("MessageSent", "extraMessage", null,
                    false, new String[]{"MessageSent"},null, "Mensaje Enviado!"));
            form.setBody(null);
        } else {
            errors.addError(new FieldError("SendMessageError", "extraMessage", null,
                    false, new String[]{"SendMessageError"}, null, "Error al enviar el mensaje."));
        }
        return course(form, form.getProfessorId(), form.getSubjectId(), null, null);
    }


    @RequestMapping("/createCourse")
    public ModelAndView createCourse(@ModelAttribute("CourseForm") final CourseForm form,
                                     @ModelAttribute("currentUser") final User user) {
        final ModelAndView mav = new ModelAndView("createCourse");
        mav.addObject("subjects", subjectService.getAvailableSubjects(user.getId()));
        return mav;
    }

    @RequestMapping(value = "/createCourse", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("CourseForm") final CourseForm form,
                               final BindingResult errors,
                               @ModelAttribute("currentUser") final User user) {
        if(errors.hasErrors()) {
            return createCourse(form, user);
        }

        final Course course;
        try {
            course = courseService.create(user.getId(), form.getSubjectId(), form.getDescription(), form.getPrice());
        } catch (CourseAlreadyExistsException e) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","courseAlreadyExists");
            return error;
        } catch (NonexistentProfessorException e) {
            final ModelAndView error = new ModelAndView("error");
            error.addObject("errorMessageCode","nonExistentUser");
            return error;
        } catch (NonexistentSubjectException e) {
            errors.addError(new FieldError("subjectDoesNotExist", "subjectId", null,
                    false, new String[]{"subjectDoesNotExist"},null, "La materia que quiere dictar no existe"));
            return createCourse(form, user);
        }

        final RedirectView view = new RedirectView("/Course/?professor=" + course.getProfessor().getId()
                + "&subject=" + course.getSubject().getId());
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

}
