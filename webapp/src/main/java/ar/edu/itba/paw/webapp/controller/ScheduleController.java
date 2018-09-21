package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.InvalidTimeException;
import ar.edu.itba.paw.exceptions.InvalidTimeRangeException;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService ss;

    @RequestMapping(value = "/CreateTimeSlot", method = RequestMethod.POST)
    public void createTimeslot(
            @RequestParam(value = "professor_id", required = true) final Long professor_id,
            @RequestParam(value = "day", required = true) final Integer day,
            @RequestParam(value = "startHour", required = true) final Integer startHour,
            @RequestParam(value = "endHour", required = true) final Integer endHour
    ){
        try {
            ss.reserveTimeSlot(professor_id, day, startHour, endHour);
        } catch (InvalidTimeException e) {
            //TODO: alguna de las horas no tienen sentido (numeros negativos o mayores a su tope) . Redirigir.
        } catch (InvalidTimeRangeException e) {
            //TODO: startHour es mas grande que endHour, manejar el error
        }
    }
}
