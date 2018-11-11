package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.ClassReservationForm;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    @Autowired
    private ClassReservationService classReservationService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @ModelAttribute("messageForm") final MessageForm messageForm,
            @ModelAttribute("commentForm") final CommentForm commentForm,
            @RequestParam(value="professor", required=true) final long professorId,
            @RequestParam(value="subject", required=true) final long subjectId,
            @RequestParam(value = "page", defaultValue = "1") final int page){
        final ModelAndView mav = new ModelAndView("course");
        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return redirectToErrorPage("nonExistentCourse");
        }
        mav.addObject("course", course);
        LOGGER.debug("Creating view for Course with professor id {} and subject id {}", professorId, subjectId);

        final Schedule schedule = scheduleService.getScheduleForProfessor(professorId);

        final PagedResults<Comment> comments = courseService.getComments(course, page);

        if(comments == null) {
            redirectToErrorPage("pageOutOfBounds");
        }

        mav.addObject("comments", comments);

        mav.addObject("schedule", schedule);
        mav.addObject("page", page);
        messageForm.setProfessorId(professorId);
        messageForm.setSubjectId(subjectId);
        commentForm.setCommentProfessorId(professorId);
        commentForm.setCommentSubjectId(subjectId);
        return mav;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ModelAndView contact(
            @ModelAttribute("commentForm") final CommentForm comment,
            @Valid @ModelAttribute("messageForm") final MessageForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) throws UserNotInConversationException, NonexistentConversationException {

        if(errors.hasErrors()) {
            return course(form,comment, form.getProfessorId(), form.getSubjectId(), 1);
        }

        final boolean sent;
        try {
            sent = conversationService.sendMessage(loggedUser.getId(), form.getProfessorId(),
                    form.getSubjectId(), form.getBody());
        } catch (SameUserException e) {
            errors.rejectValue("body", "SameUserMessageError");
            form.setBody(null);
            return course(form,comment, form.getProfessorId(), form.getSubjectId(),1);
        }
        if(sent) {
            errors.rejectValue("extraMessage", "MessageSent");
            form.setBody(null);
            return course(form,comment, form.getProfessorId(), form.getSubjectId(),1);
        } else {
            errors.rejectValue("body", "SendMessageError");
        }
        return course(form,comment, form.getProfessorId(), form.getSubjectId(),1);
    }

    @RequestMapping(value = "/postComment", method = RequestMethod.POST)
    public ModelAndView comment(
            @ModelAttribute("messageForm") final MessageForm message,
            @Valid @ModelAttribute("commentForm") final CommentForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) {

        if(errors.hasErrors()) {
            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(),1);
        }

        final boolean sent = courseService.comment(loggedUser.getId(), form.getCommentProfessorId(),
                    form.getCommentSubjectId(), form.getCommentBody(), form.getRating());
        if(sent) {
            return redirectWithNoExposedModalAttributes("/Course/?professor=" +
                    form.getCommentProfessorId() + "&subject=" + form.getCommentSubjectId());
        } else {
            errors.rejectValue("commentBody", "SendMessageError");
        }
        return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(),1);
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

    @RequestMapping(value = "/reserveClass", method = RequestMethod.GET)
    public ModelAndView reserveClass(@ModelAttribute("currentUser") final User user,
                                     @ModelAttribute("classReservationForm") final ClassReservationForm form,
                                     @RequestParam("professor") final long professorId,
                                     @RequestParam("subject") final long subjectId) {
        final ModelAndView mav = new ModelAndView("reserveClass");
        final Schedule schedule = scheduleService.getScheduleForProfessor(professorId);
        mav.addObject("schedule", schedule);
        return mav;
    }

    @RequestMapping(value = "/reserveClass", method = RequestMethod.POST)
    public ModelAndView reserveClass(@ModelAttribute("currentUser") final User user,
                                     @Valid @ModelAttribute("classReservationForm")
                                     final ClassReservationForm form,
                                     final BindingResult errors,
                                     @RequestParam("professor") final long professorId,
                                     @RequestParam("subject") final long subjectId) {

        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return redirectToErrorPage("nonExistentCourse");
        }
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            return reserveClass(user, form, professorId, subjectId);
        }

        final LocalDate day = new LocalDate(form.getDay());

        final LocalDateTime startTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getStartHour(), 0);

        final LocalDateTime endTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getEndHour(), 0);

        ClassReservation reservation = null;
        try {
            reservation = classReservationService.reserve(startTime, endTime,
                    course, user.getId());
        } catch (SameUserException e) {
            return redirectToErrorPage("sameUserReservation");
        }

        if(reservation == null){
            return redirectToErrorPage("");
        }
        return redirectWithNoExposedModalAttributes("/Course/?professor=" + professorId
                + "&subject=" + subjectId);
    }

    @InitBinder
    private void dateBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }

}
