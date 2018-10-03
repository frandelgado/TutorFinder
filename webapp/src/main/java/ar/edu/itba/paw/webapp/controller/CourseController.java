package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;


@Controller
public class CourseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

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
            @ModelAttribute(value = "SUCCESS_MESSAGE") final String success_message,
            @ModelAttribute(value = "ERROR_MESSAGE") final String error_message
    ){
        final ModelAndView mav = new ModelAndView("course");
        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return redirectToErrorPage("nonExistentCourse");
        }
        mav.addObject("course", course);
        LOGGER.debug("Creating view for Course with professor id {} and subject id {}", professorId, subjectId);

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
            errors.rejectValue("body", "SameUserMessageError");
            form.setBody(null);
            return course(form, form.getProfessorId(), form.getSubjectId(), null, null);
        }
        if(sent) {
            errors.rejectValue("extraMessage", "MessageSent");
            form.setBody(null);
            return course(form, form.getProfessorId(), form.getSubjectId(), null, null);
        } else {
            errors.rejectValue("body", "SendMessageError");
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
            return redirectToErrorPage("courseAlreadyExists");
        } catch (NonexistentProfessorException e) {
            return redirectToErrorPage("nonExistentUser");
        } catch (NonexistentSubjectException e) {
            errors.rejectValue("subjectId", "subjectDoesNotExist");
            return createCourse(form, user);
        }

        if(course == null) {
            return createCourse(form, user);
        }

        LOGGER.debug("Posting request for course creation for professor with id {} in subject with id {}", user.getId(), form.getSubjectId());
        return redirectWithNoExposedModalAttributes("/Course/?professor=" + course.getProfessor().getId()
                + "&subject=" + course.getSubject().getId());
    }

}
