package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
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

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return new ModelAndView("403");
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessageCode", "404");
        return mav;
    }

}
