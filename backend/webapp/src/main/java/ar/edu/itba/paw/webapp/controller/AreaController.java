package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.webapp.dto.AreaDTO;
import ar.edu.itba.paw.webapp.dto.AreaListDTO;
import ar.edu.itba.paw.webapp.dto.CourseListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("areas")
@Component
public class AreaController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @Autowired
    private CourseService cs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listAreas(@DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Area> allAreas = as.filterAreasByName("", page);

        final Link[] links = new Link[1];

        return Response.ok(new AreaListDTO(allAreas.getResults(), uriInfo.getBaseUri())).links(links).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response area(@PathParam("id") final long id) {
        final Area area = as.findAreaById(id);

        if(area == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(new AreaDTO(area, uriInfo.getBaseUri())).build();
    }

    @GET
    @Path("/{id}/courses")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response area(@PathParam("id") final long id,
                         @DefaultValue("1") @QueryParam("page") final int page) {

        final Area area = as.findAreaById(id);
        if(area == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.debug("Creating view for Area with id {}", id);

        final PagedResults<Course> results = cs.filterByAreaId(id, page);

        if(results == null) {
            return Response.noContent().build(); //FIXME: Cuando cambie paginacion sacar chequeo.
        }

        return Response.ok(new CourseListDTO(results.getResults(), uriInfo.getBaseUri())).build();
    }
}
