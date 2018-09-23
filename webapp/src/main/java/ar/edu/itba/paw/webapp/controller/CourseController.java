package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.SameUserConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
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
    @Qualifier("professorServiceImpl")
    private ProfessorService professorService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @ModelAttribute("messageForm") final MessageForm form,
            @RequestParam(value="professor", required=true) final Long professorId,
            @RequestParam(value="subject", required=true) final Long subjectId
    ){
        final ModelAndView mav = new ModelAndView("course");
        mav.addObject("course", courseService.findCourseByIds(professorId, subjectId));
        form.setProfessorId(professorId);
        form.setSubjectId(subjectId);
        return mav;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ModelAndView contact(
            @Valid @ModelAttribute("messageForm") final MessageForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) throws SameUserConversationException, UserNotInConversationException {

        if(errors.hasErrors()) {
            return course(form, form.getProfessorId(), form.getSubjectId());
        }

        final User user = userService.findUserById(loggedUser.getId());
        final Professor professor = professorService.findById(form.getProfessorId());
        final Subject subject = subjectService.findSubjectById(form.getSubjectId());

        boolean sent = conversationService.sendMessage(user, professor, subject, form.getBody());
        if(sent) {
            errors.addError(new FieldError("MessageSent", "extraMessage", null,
                    false, new String[]{"MessageSent"},null, "Mensaje Enviado!"));
            form.setBody(null);
            return course(form, form.getProfessorId(), form.getSubjectId());
        }
        errors.addError(new FieldError("SendMessageError", "extraMessage", null,
                false, new String[]{"SendMessageError"},null, "Error al enviar el mensaje."));
        return course(form, form.getProfessorId(), form.getSubjectId());
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

        final Professor professor = professorService.findById(user.getId());

        final Subject subject = subjectService.findSubjectById(form.getSubjectId());

        //TODO: Catch null

        final Course course = courseService.create(professor, subject, form.getDescription(), form.getPrice());


        final RedirectView view = new RedirectView("/Course/?professor=" + course.getProfessor().getId()
                + "&subject=" + course.getSubject().getId());
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

}
