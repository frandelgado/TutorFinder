package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.CourseFileListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("courses/{professor}_{subject}/files")
@Component
public class CourseFileController extends BaseController {

    @Autowired
    private CourseFileService cfs;

    @Autowired
    private CourseService cs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getCourseFiles(@PathParam("professor") final long professorId,
                                   @PathParam("subject") final long subjectId) {

        final User currentUser = loggedUser();
        final Course course = cs.findCourseByIds(professorId, subjectId);

        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final List<CourseFile> courseFiles;
        try {
            courseFiles = cfs.findForCourse(course, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(new CourseFileListDTO(courseFiles, uriInfo.getBaseUri())).build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_OCTET_STREAM, })
    public Response downloadFile(@PathParam("professor") final long professorId,
                                 @PathParam("subject") final long subjectId,
                                 @PathParam("id") final long id) {

        final User currentUser = loggedUser();
        final CourseFile courseFile;
        try {
            courseFile = cfs.findByIdForUser(id, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(courseFile == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //TODO: Verificar si el header es necesario
        return Response
                .ok(courseFile.getContent(), MediaType.valueOf(courseFile.getType()))
                .header("Content-Disposition","attachment; filename=\"" + courseFile.getName() +"\"")
                .build();
    }


//    @PUT
//    @Path("/{id}")
//    public ModelAndView uploadFile(@Valid @ModelAttribute("uploadClassFileForm") final UploadClassFileForm form,
//                                   BindingResult result,
//                                   @RequestParam("professor") final long professorId,
//                                   @RequestParam("subject") final long subjectId,
//                                   @ModelAttribute("currentUser") final User currentUser) {
//
//        CourseFile courseFile = null;
//        if(result.hasErrors()) {
//            return getCourseFiles(professorId, subjectId, currentUser, form);
//        }
//
//        try {
//            courseFile = cfs.save(professorId, subjectId, currentUser, form.getFile().getOriginalFilename(),
//                    form.getDescription(), form.getFile().getContentType(), form.getFile().getBytes());
//        } catch (IOException e) {
//            return redirectToErrorPage("oops");
//        } catch (UserAuthenticationException e) {
//            return redirectToErrorPage("403");
//        }
//
//        if(courseFile == null) {
//            return getCourseFiles(professorId, subjectId, currentUser, form);
//        }
//
//        return redirectWithNoExposedModalAttributes("/courseFiles?professor=" + professorId
//                +"&subject=" + subjectId);
//    }

    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam("professor") final long professorId,
                                   @PathParam("subject") final long subjectId,
                                   @PathParam("id") final long id) {

        final User currentUser = loggedUser();
        try {
            cfs.deleteById(id, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
}
