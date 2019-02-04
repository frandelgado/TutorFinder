package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.DTOConstraintException;
import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.form.MessageForm;
import ar.edu.itba.paw.webapp.validator.DTOConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;


@Path("conversations")
@Component
public class ConversationController extends BaseController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private DTOConstraintValidator validator;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response conversations(@DefaultValue("1") @QueryParam("page") final int page) {

        final User loggedUser = loggedUser();
        final PagedResults<Conversation> conversations = conversationService.findByUserId(loggedUser.getId(), page);

        if(conversations == null) {
            return Response.noContent().build(); //TODO: Cuando cambie paginacion sacar chequeo.
        }

        return Response.ok(new ConversationListDTO(conversations.getResults(), uriInfo.getBaseUri())).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response conversation(@PathParam("id") final long id) {

        final User loggedUser = loggedUser();
        final Conversation conversation;
        try {
            conversation = conversationService.findById(id, loggedUser.getId());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(new ConversationDTO(conversation, uriInfo.getBaseUri())).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}/messages")
    public Response messages(@PathParam("id") final long id) {

        final User loggedUser = loggedUser();
        final Conversation conversation;
        try {
            conversation = conversationService.findById(id, loggedUser.getId());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Conversation ret = conversationService.initializeMessages(conversation);

        return Response.ok(new MessageListDTO(ret.getMessages(), uriInfo.getBaseUri())).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response sendMessage(@PathParam("id") final long id, final MessageForm message)
            throws DTOConstraintException {

        validator.validate(message);

        final User loggedUser = loggedUser();

        final boolean sent;
        try {
            sent = conversationService.sendMessage(loggedUser.getId(), id, message.getMessage());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NonexistentConversationException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!sent) {
            final ValidationErrorDTO errors = new ValidationErrorDTO("Error sending message");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errors).build();
        }

        final URI uri = uriInfo.getAbsolutePathBuilder().path("/messages").build();
        return Response.created(uri).build();
    }

}
