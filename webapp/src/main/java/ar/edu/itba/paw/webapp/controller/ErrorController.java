package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
@Controller
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError() {

        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessageCode", "somethingWentWrong");
        return mav;
    }

    @ExceptionHandler({UserNotInConversationException.class, NonexistentConversationException.class})
    public ModelAndView handleConversationExceptions(Exception e) {
        final ModelAndView error = new ModelAndView("error");
        final String messageCode;
        if(e instanceof UserNotInConversationException) {
            messageCode = "userNotInConversation";
        } else {
            messageCode = "nonExistentConversation";
        }
        error.addObject("errorMessageCode", messageCode);
        return error;
    }

    @ExceptionHandler(PageOutOfBoundsException.class)
    public ModelAndView handlePageOutOfBounds(PageOutOfBoundsException e) {
        final ModelAndView error = new ModelAndView("error");
        error.addObject("errorMessageCode", "pageOutOfBounds");
        return error;
    }

    @ExceptionHandler({NonexistentProfessorException.class, ProfessorWithoutUserException.class})
    public ModelAndView handleNonexistentUser() {
        final ModelAndView error = new ModelAndView("error");
        error.addObject("errorMessageCode","nonExistentUser");
        return error;
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessageCode", "403");
        return mav;
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessageCode", "404");
        return mav;
    }

}
