package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.ClassReservationDTO;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("user")
@Component
public class UserController extends BaseController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ClassReservationService classReservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @Context
    private UriInfo uriInfo;


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response profile() {

        final User loggedUser = loggedUser();
        final Professor professor = professorService.findById(loggedUser.getId());

        //TODO: Everything
        return Response.ok().build();
    }

    @GET
    @Path("/reservations")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservations(@DefaultValue("1") @QueryParam("page") final int page) {
        final User loggedUser = loggedUser();

        final PagedResults<ClassReservation> classReservations =  userService.pagedReservations(loggedUser.getId(), page);

        if(classReservations == null) {
            return badRequest("Invalid page number");
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, classReservations);

        final GenericEntity<List<ClassReservationDTO>> entity = new GenericEntity<List<ClassReservationDTO>>(
                classReservations.getResults().stream()
                        .map(reservation -> new ClassReservationDTO(reservation, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/reservations/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservation(@PathParam("id") final long id) {
        final User loggedUser = loggedUser();

        final ClassReservation classReservation = classReservationService.findById(id);

        if(classReservation == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if(!classReservation.getStudent().getId().equals(loggedUser.getId()))
            return Response.status(Response.Status.FORBIDDEN).build();

        return Response.ok(new ClassReservationDTO(classReservation, uriInfo)).build();
    }

    @GET
    @Path("/requests")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response requests(@DefaultValue("1") @QueryParam("page") final int page) {
        final User loggedUser = loggedUser();

        final PagedResults<ClassReservation> classRequests;
        try {
            classRequests = professorService.getPagedClassRequests(loggedUser.getId(), page);
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classRequests == null) {
            return badRequest("Invalid page number");
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, classRequests);

        final GenericEntity<List<ClassReservationDTO>> entity = new GenericEntity<List<ClassReservationDTO>>(
                classRequests.getResults().stream()
                        .map(request -> new ClassReservationDTO(request, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/requests/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response request(@PathParam("id") final long id) {
        return reservation(id);
    }

    @PUT
    @Path("/requests/{id}")
    public Response approveClassRequest(@PathParam("id") final long id) {

        final User currentUser = loggedUser();

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.confirm(id, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classReservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/requests/{id}")
    public Response denyClassRequest(@PathParam("id") final long id) {

        final User currentUser = loggedUser();

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.deny(id, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classReservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build();
    }
}
