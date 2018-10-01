package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BaseController {

    public ModelAndView redirectToErrorPage(final String errorCode) {
        final ModelAndView error = new ModelAndView("error");
        error.addObject("errorMessageCode",errorCode);
        return error;
    }

    public ModelAndView redirectWithNoExposedModalAttributes(final String url) {
        final RedirectView view = new RedirectView(url, true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
}
