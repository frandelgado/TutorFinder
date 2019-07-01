package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.CommentListDTO;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.CourseListDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.dto.form.CommentForm;
import ar.edu.itba.paw.webapp.dto.form.CourseForm;
import ar.edu.itba.paw.webapp.dto.form.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
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
    public Response courses(@DefaultValue("") @QueryParam("q") final String query,
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

//    @POST
//    @Path("{professor}_{subject}/contact")
//    @Consumes(value = { MediaType.APPLICATION_JSON, })
//    public Response contact(@PathParam("professor") final long professorId,
//                            @PathParam("subject") final long subjectId,
//                            final MessageForm message) throws DTOConstraintException {
//
//        validator.validate(message);
//
//        final User loggedUser = loggedUser();
//        final boolean sent;
//
//        try {
//            sent = conversationService.sendMessage(loggedUser.getId(), professorId, subjectId, message.getMessage());
//        } catch (SameUserException e) {
//            return badRequest("Can not contact yourself");
//        } catch (UserNotInConversationException e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        } catch (NonexistentConversationException e) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        if(!sent) {
//            return badRequest("Error sending message");
//        }
//
//        final URI uri = uriInfo.getBaseUriBuilder().path("/conversations/").build();
//        return Response.created(uri).build();
//    }

    @POST
    @Path("{professor}_{subject}/comments")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response comment(@Valid final CommentForm comment,
                            @PathParam("professor") final long professorId,
                            @PathParam("subject") final long subjectId) throws ConstraintViolationException {

        final User loggedUser = loggedUser();

        final boolean sent;
        try {
            sent = courseService.comment(loggedUser.getId(), professorId, subjectId,
                    comment.getCommentBody(), comment.getRating());
        } catch (SameUserException e) {
            return badRequest("Cannot comment on your course");
        } catch (NonAcceptedReservationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(!sent) {
            return badRequest("Error commenting");
        }

        //TODO: Ver si agregar page
        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response create(@Valid final CourseForm form) throws ConstraintViolationException {

        final User user = loggedUser();

        final Course course;
        try {
            course = courseService.create(user.getId(), form.getSubject(), form.getDescription(), form.getPrice());
        } catch (CourseAlreadyExistsException e) {
            final ValidationErrorDTO errors = new ValidationErrorDTO("Course already exists");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NonexistentSubjectException e) {
            return badRequest("Nonexistent Subject");
        }

        if(course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        LOGGER.debug("Posting request for course creation for professor with id {} in subject with id {}", user.getId(), form.getSubject());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId()) +
                "_" + String.valueOf(form.getSubject())).build();
        return Response.created(uri).build();
    }

//    @POST
//    @Path("{professor}_{subject}/reservations")
//    @Consumes(value = { MediaType.APPLICATION_JSON, })
//    public Response reserveClass(@ModelAttribute("currentUser") final User user,
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


    //TODO: Approve or deny (Ideas: put y delete / form with comment + status)
    @POST
    @Path("/requests/{id}")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response answerClassRequest(@PathParam("id") final long classReservationId) {

        final User currentUser = loggedUser();

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.deny(classReservationId, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classReservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build();
    }

//    @InitBinder
//    private void dateBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
//        binder.registerCustomEditor(Date.class, editor);
//    }

    @PUT
    @Path("{professor}_{subject}")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response modify(@Valid final CourseForm form,
                           @PathParam("professor") final long professorId,
                           @PathParam("subject") final long subjectId) throws ConstraintViolationException {

        final User user = loggedUser();

        final Course course;
        try {
            course = courseService.modify(user.getId(), subjectId, form.getDescription(), form.getPrice());
        } catch (NonexistentCourseException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        LOGGER.debug("Posting request for course modification for professor with id {} in subject with id {}", user.getId(), subjectId);
        return Response.noContent().build();
    }

}
