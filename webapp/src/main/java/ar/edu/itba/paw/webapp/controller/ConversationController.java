package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;


@Controller
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/Conversations")
    public ModelAndView conversations(@ModelAttribute("currentUser") final User loggedUser,
                                      @RequestParam(value="page", defaultValue="1") final int page) throws PageOutOfBoundsException {
        final ModelAndView mav = new ModelAndView("conversations");
        mav.addObject("conversations", conversationService.findByUserId(loggedUser.getId(), page));
        mav.addObject("page", page);
        return mav;
    }

    @RequestMapping("/Conversation")
    public ModelAndView conversation(@RequestParam(value="id", required=true) final Long id,
                                     @ModelAttribute("messageForm") final MessageForm form,
                                     @ModelAttribute("currentUser") final User loggedUser)
            throws UserNotInConversationException, NonexistentConversationException {

        final ModelAndView mav = new ModelAndView("conversation");
        mav.addObject("conversation", conversationService.findById(id, loggedUser.getId()));
        form.setConversationId(id);
        return mav;
    }

    @RequestMapping(value = "/Conversation", method = RequestMethod.POST)
    public ModelAndView sendMessage(@Valid @ModelAttribute("messageForm") final MessageForm form,
                                     final BindingResult errors,
                                     @ModelAttribute("currentUser") final User loggedUser)
            throws UserNotInConversationException, NonexistentConversationException {

        if(errors.hasErrors()) {
            return conversation(form.getConversationId(), form, loggedUser);
        }

        final boolean sent = conversationService.sendMessage(loggedUser.getId(), form.getConversationId(), form.getBody());
        if(sent) {
            final RedirectView view = new RedirectView("/Conversation?id=" + form.getConversationId());
            view.setExposeModelAttributes(false);
            return new ModelAndView(view);
        }
        errors.addError(new FieldError("SendMessageError", "extraMessage", null,
                false, new String[]{"SendMessageError"},null, "Error al enviar el mensaje."));
        return conversation(form.getConversationId(), form, loggedUser);
    }

}
