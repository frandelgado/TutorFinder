package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.AreaDTO;
import ar.edu.itba.paw.webapp.dto.CourseListDTO;
import ar.edu.itba.paw.webapp.dto.ProfessorDTO;
import ar.edu.itba.paw.webapp.dto.ProfessorListDTO;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("professors")
@Component
public class ProfessorController extends BaseController{


    private static final Logger LOGGER = LoggerFactory.getLogger(ar.edu.itba.paw.webapp.old_controller.UserController.class);

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
    private HttpSessionRequestCache cache;

    @Autowired
    private ScheduleService ss;

    @Autowired
    private PasswordResetService passwordResetService;


    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response listProfessors(@DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Professor> allProfessors = ps.filterByFullName("", page);

        final Link[] links= new Link[1];

        return Response.ok(new ProfessorListDTO(allProfessors.getResults(), uriInfo.getBaseUri())).links(links).build();
    }


    @GET
    @Path("/{username}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response professor(@PathParam("username") final String username,
                              @ModelAttribute("currentUser") final User loggedUser,
                              @ModelAttribute("currentUserIsProfessor") final boolean isProfessor){
        final Professor professor = ps.findByUsername(username);

        if(professor == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ProfessorDTO(professor, uriInfo.getBaseUri())).build();

    }
}
