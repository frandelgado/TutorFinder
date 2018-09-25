package ar.edu.itba.paw.webapp.controller;

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

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return new ModelAndView("403");
    }

}
