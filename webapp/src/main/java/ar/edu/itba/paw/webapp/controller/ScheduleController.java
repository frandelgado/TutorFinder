package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.webapp.form.ScheduleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ScheduleController {

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    private ScheduleService ss;

    /*@RequestMapping(value = "/CreateTimeSlot", method = RequestMethod.POST)
    public void createTimeslot(
            @Valid @ModelAttribute("newScheduleForm") final ScheduleForm form,
            final BindingResult errors
    ){
        *//*if(errors.hasErrors()) {
            return register(form);
        }*//*

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();

        final Professor professor = ps.findByUsername(username);

        try {
            ss.reserveTimeSlot(professor.getId(), form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (InvalidTimeException e) {
            //TODO: alguna de las horas no tienen sentido (numeros negativos o mayores a su tope) . Redirigir.
        } catch (InvalidTimeRangeException e) {
            //TODO: startHour es mas grande que endHour, manejar el error
        }
    }*/
}
