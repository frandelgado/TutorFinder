package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
@Controller
public class ErrorController extends BaseController{

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError() {
        return redirectToErrorPage("somethingWentWrong");
    }

    @ExceptionHandler({UserNotInConversationException.class, NonexistentConversationException.class})
    public ModelAndView handleConversationExceptions(Exception e) {
        final String messageCode;
        if(e instanceof UserNotInConversationException) {
            messageCode = "userNotInConversation";
        } else {
            messageCode = "nonExistentConversation";
        }
        return redirectToErrorPage(messageCode);
    }

    @ExceptionHandler(PageOutOfBoundsException.class)
    public ModelAndView handlePageOutOfBounds() {
        return redirectToErrorPage("pageOutOfBounds");
    }

    @ExceptionHandler({NonexistentProfessorException.class, ProfessorWithoutUserException.class})
    public ModelAndView handleNonexistentUser() {
        return redirectToErrorPage("nonExistentUser");
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return redirectToErrorPage("403");
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        return redirectToErrorPage("404");
    }

    @RequestMapping("/500")
    public ModelAndView internalError() {
        return redirectToErrorPage("500");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleFileSizeException() {
        final ModelAndView mav = new ModelAndView("registerAsProfessorForm");
        return mav;
    }

    @RequestMapping("/500")
    public ModelAndView internalError() {
        return redirectToErrorPage("500");
    }
}
