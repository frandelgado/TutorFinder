package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.exceptions.SameUserConversationException;
import ar.edu.itba.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Qualifier("subjectServiceImpl")
    private SubjectService subjectService;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService professorService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/Conversations")
    public ModelAndView conversations(@ModelAttribute("currentUser") final User loggedUser){
        final ModelAndView mav = new ModelAndView("conversations");
        mav.addObject("conversations", conversationService.findByUserId(loggedUser.getId()));
        return mav;
    }

    @RequestMapping("/Conversation")
    public ModelAndView conversation(@RequestParam(value="id", required=true) final Long id,
                                     @ModelAttribute("messageForm") final MessageForm form,
                                     @ModelAttribute("currentUser") final User loggedUser) throws UserNotInConversationException {

        final ModelAndView mav = new ModelAndView("conversation");
        mav.addObject("conversation", conversationService.findById(id, loggedUser.getId()));
        form.setConversationId(id);
        return mav;
    }

    @RequestMapping(value = "/Conversation", method = RequestMethod.POST)
    public ModelAndView sendMessage(@Valid @ModelAttribute("messageForm") final MessageForm form,
                                     final BindingResult errors,
                                     @ModelAttribute("currentUser") final User loggedUser)
            throws UserNotInConversationException {

        if(errors.hasErrors()) {
            return conversation(form.getConversationId(), form, loggedUser);
        }

        final User user = userService.findUserById(loggedUser.getId());
        final Conversation conversation = conversationService.findById(form.getConversationId(), loggedUser.getId());

        boolean sent = conversationService.sendMessage(user, conversation, form.getBody());
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
