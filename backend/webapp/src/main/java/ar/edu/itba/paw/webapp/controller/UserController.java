package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
@Component
public class UserController extends BaseController {

    @Autowired
    private ProfessorService professorService;


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response profile() {

        final User loggedUser = loggedUser();
        final Professor professor = professorService.findById(loggedUser.getId());



    }
}
