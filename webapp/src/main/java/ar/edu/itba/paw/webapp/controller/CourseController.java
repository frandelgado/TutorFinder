package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.CommentListDTO;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.CourseListDTO;
import ar.edu.itba.paw.webapp.form.ClassReservationForm;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Path("courses")
@Component
public class CourseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassReservationService classReservationService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("{professor}_{subject}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response course(@PathParam("professor") final long professorId,
                           @PathParam("subject") final long subjectId){

        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.debug("Creating view for Course with professor id {} and subject id {}", professorId, subjectId);

        return Response.ok(new CourseDTO(course, uriInfo.getBaseUri())).build();
    }

    @GET
    @Path("{professor}_{subject}/comments")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response comments(@PathParam("professor") final long professorId,
                             @PathParam("subject") final long subjectId,
                             @DefaultValue("1") @QueryParam("page") final int page) {

        final Course course = courseService.findCourseByIds(professorId, subjectId);

        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final PagedResults<Comment> comments = courseService.getComments(course, page);

        if(comments == null) {
            return Response.noContent().build(); //TODO: Cuando cambie paginacion sacar chequeo.
        }

        return Response.ok(new CommentListDTO(comments.getResults(), uriInfo.getBaseUri())).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response courses(@QueryParam("q") final String query,
                            @QueryParam("start") final Integer startHour,
                            @QueryParam("end") final Integer endHour,
                            @QueryParam("min") final Double minPrice,
                            @QueryParam("max") final Double maxPrice,
                            @QueryParam("days") final List<Integer> days,
                            @DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Course> courses = courseService.filterCourses(days, startHour, endHour,
                minPrice, maxPrice, query, page);

        final Link[] links = new Link[1];

        return Response.ok(new CourseListDTO(courses.getResults(), uriInfo.getBaseUri())).links(links).build();
    }

//    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
//    public ModelAndView contact(
//            @ModelAttribute("commentForm") final CommentForm comment,
//            @Valid @ModelAttribute("messageForm") final MessageForm form,
//            final BindingResult errors,
//            @ModelAttribute("currentUser") final User loggedUser) throws UserNotInConversationException, NonexistentConversationException {
//
//        if(errors.hasErrors()) {
//            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser, 1);
//        }
//
//        final boolean sent;
//        try {
//            sent = conversationService.sendMessage(loggedUser.getId(), form.getProfessorId(),
//                    form.getSubjectId(), form.getBody());
//        } catch (SameUserException e) {
//            errors.rejectValue("body", "SameUserMessageError");
//            form.setBody(null);
//            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
//        }
//        if(sent) {
//            errors.rejectValue("extraMessage", "MessageSent");
//            form.setBody(null);
//            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
//        } else {
//            errors.rejectValue("body", "SendMessageError");
//        }
//        return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
//    }

//    @RequestMapping(value = "/postComment", method = RequestMethod.POST)
//    public ModelAndView comment(
//            @ModelAttribute("messageForm") final MessageForm message,
//            @Valid @ModelAttribute("commentForm") final CommentForm form,
//            final BindingResult errors,
//            @ModelAttribute("currentUser") final User loggedUser) {
//
//        if(errors.hasErrors()) {
//            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
//        }
//
//        final boolean sent;
//        try {
//            sent = courseService.comment(loggedUser.getId(), form.getCommentProfessorId(),
//                        form.getCommentSubjectId(), form.getCommentBody(), form.getRating());
//        } catch (SameUserException e) {
//            errors.rejectValue("rating", "sameUserComment");
//            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
//        } catch (NonAcceptedReservationException e) {
//            errors.rejectValue("rating", "nonAcceptedReservation");
//            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
//        }
//
//        if(sent) {
//            return redirectWithNoExposedModalAttributes("/Course/?professor=" +
//                    form.getCommentProfessorId() + "&subject=" + form.getCommentSubjectId());
//        } else {
//            errors.rejectValue("commentBody", "SendMessageError");
//        }
//        return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
//    }

//    @RequestMapping(value = "/createCourse", method = RequestMethod.POST)
//    public ModelAndView create(@Valid @ModelAttribute("CourseForm") final CourseForm form,
//                               final BindingResult errors,
//                               @ModelAttribute("currentUser") final User user) {
//        if(errors.hasErrors()) {
//            return createCourse(form, user);
//        }
//
//        final Course course;
//        try {
//            course = courseService.create(user.getId(), form.getSubjectId(), form.getDescription(), form.getPrice());
//        } catch (CourseAlreadyExistsException e) {
//            return redirectToErrorPage("courseAlreadyExists");
//        } catch (NonexistentProfessorException e) {
//            return redirectToErrorPage("nonExistentUser");
//        } catch (NonexistentSubjectException e) {
//            errors.rejectValue("subjectId", "subjectDoesNotExist");
//            return createCourse(form, user);
//        }
//
//        if(course == null) {
//            return createCourse(form, user);
//        }
//
//        LOGGER.debug("Posting request for course creation for professor with id {} in subject with id {}", user.getId(), form.getSubjectId());
//        return redirectWithNoExposedModalAttributes("/Course/?professor=" + course.getProfessor().getId()
//                + "&subject=" + course.getSubject().getId());
//    }

//    @RequestMapping(value = "/reserveClass", method = RequestMethod.POST)
//    public ModelAndView reserveClass(@ModelAttribute("currentUser") final User user,
//                                     @Valid @ModelAttribute("classReservationForm")
//                                     final ClassReservationForm form,
//                                     final BindingResult errors,
//                                     @RequestParam("professor") final long professorId,
//                                     @RequestParam("subject") final long subjectId) {
//
//        if(errors.hasErrors() || !form.validForm()) {
//            if(!form.validForm()) {
//                errors.rejectValue("endHour", "profile.add_schedule.timeError");
//            }
//            return reserveClass(user, form, professorId, subjectId);
//        }
//
//        final LocalDate day = new LocalDate(form.getDay());
//
//        final LocalDateTime startTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
//                day.getDayOfMonth(), form.getStartHour(), 0);
//
//        final LocalDateTime endTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
//                day.getDayOfMonth(), form.getEndHour(), 0);
//
//        if(!startTime.isAfter(LocalDateTime.now())) {
//            errors.rejectValue("day", "futureDateError");
//            return reserveClass(user, form, professorId, subjectId);
//        }
//
//        final ClassReservation reservation;
//        try {
//            reservation = classReservationService.reserve(startTime, endTime,
//                    professorId, subjectId, user.getId());
//        } catch (SameUserException e) {
//            return redirectToErrorPage("sameUserReservation");
//        } catch (NonexistentCourseException e) {
//            return redirectToErrorPage("nonExistentCourse");
//        } catch (NonExistentUserException e) {
//            return redirectToErrorPage("nonExistentUser");
//        } catch (ReservationTimeOutOfRange reservationTimeOutOfRange) {
//            errors.rejectValue("day", "notAvailableTime");
//            return reserveClass(user, form, professorId, subjectId);
//        }
//
//        if(reservation == null) {
//            return reserveClass(user, form, professorId, subjectId);
//        }
//
//        return redirectWithNoExposedModalAttributes("/reservations");
//    }


//    @RequestMapping("/denyClassRequest")
//    public ModelAndView denyClassRequest(@ModelAttribute("currentUser") final User currentUser,
//                                         @RequestParam("classReservation") final long classReservationId) {
//
//        final ClassReservation classReservation;
//        try {
//            classReservation = classReservationService.deny(classReservationId, currentUser.getId(), null);
//        } catch (UserAuthenticationException e) {
//            return redirectToErrorPage("403");
//        }
//
//        if(classReservation == null) {
//            return redirectToErrorPage("nonExistentClassReservation");
//        }
//
//        return redirectWithNoExposedModalAttributes("/classRequests");
//    }
//
//    @RequestMapping("/approveClassRequest")
//    public ModelAndView approveClassRequest(@ModelAttribute("currentUser") final User currentUser,
//                                            @RequestParam("classReservation") final long classReservationId) {
//
//        final ClassReservation classReservation;
//        try {
//            classReservation = classReservationService.confirm(classReservationId, currentUser.getId(), null);
//        } catch (UserAuthenticationException e) {
//            return redirectToErrorPage("403");
//        }
//
//        if(classReservation == null) {
//            return redirectToErrorPage("nonExistentClassReservation");
//        }
//
//        return redirectWithNoExposedModalAttributes("/classRequests");
//    }


//    @InitBinder
//    private void dateBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
//        binder.registerCustomEditor(Date.class, editor);
//    }

//    @RequestMapping(value = "/modifyCourse", method = RequestMethod.POST)
//    public ModelAndView modify(@Valid @ModelAttribute("modifyForm") final CourseForm form,
//                               final BindingResult errors,
//                               @ModelAttribute("currentUser") final User user) {
//        if(errors.hasErrors()) {
//            return modifyCourse(form, user, form.getSubjectId());
//        }
//
//        final Course course;
//        try {
//            course = courseService.modify(user.getId(), form.getSubjectId(), form.getDescription(), form.getPrice());
//        } catch (NonexistentCourseException e) {
//            return redirectToErrorPage("nonExistentCourse");
//        }
//
//        if(course == null) {
//            return modifyCourse(form, user, form.getSubjectId());
//        }
//
//        LOGGER.debug("Posting request for course modification for professor with id {} in subject with id {}", user.getId(), form.getSubjectId());
//        return redirectWithNoExposedModalAttributes("/Course/?professor=" + course.getProfessor().getId()
//                + "&subject=" + course.getSubject().getId());
//    }

}
