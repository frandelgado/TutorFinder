package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.ConversationDTO;
import ar.edu.itba.paw.webapp.dto.ConversationListDTO;
import ar.edu.itba.paw.webapp.dto.MessageListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("conversations")
@Component
public class ConversationController extends BaseController {

    @Autowired
    private ConversationService conversationService;

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

//    @RequestMapping(value = "/Conversation", method = RequestMethod.POST)
//    public ModelAndView sendMessage(@Valid @ModelAttribute("messageForm") final MessageForm form,
//                                     final BindingResult errors,
//                                     @ModelAttribute("currentUser") final User loggedUser)
//            throws UserNotInConversationException, NonexistentConversationException {
//
//        if(errors.hasErrors()) {
//            return conversation(form.getConversationId(), form, loggedUser);
//        }
//
//        final boolean sent = conversationService.sendMessage(loggedUser.getId(), form.getConversationId(), form.getBody());
//        if(sent) {
//            return redirectWithNoExposedModalAttributes("/Conversation?id=" + form.getConversationId());
//        }
//        errors.rejectValue("body", "SendMessageError");
//        return conversation(form.getConversationId(), form, loggedUser);
//    }

}
