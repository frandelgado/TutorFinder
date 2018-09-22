package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.exceptions.SameUserConversationException;
import ar.edu.itba.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
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
    public ModelAndView conversations(){

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();
        final User user = userService.findByUsername(username);

        final ModelAndView mav = new ModelAndView("conversations");
        mav.addObject("conversations", conversationService.findByUserId(user.getId()));
        return mav;
    }

    @RequestMapping("/Conversations/")
    public ModelAndView conversation(@RequestParam(value="id", required=true) final Long id) throws UserNotInConversationException {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();
        final User user = userService.findByUsername(username);

        final ModelAndView mav = new ModelAndView("conversation");
        mav.addObject("conversation", conversationService.findById(id, user.getId()));
        return mav;
    }

}
