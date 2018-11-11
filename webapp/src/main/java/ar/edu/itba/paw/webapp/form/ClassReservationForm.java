package ar.edu.itba.paw.webapp.form;

import org.joda.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ClassReservationForm {

    @NotNull
    @Min(1)
    @Max(23)
    private Integer startHour;

    @NotNull
    @Min(2)
    @Max(24)
    private Integer endHour;

    @Future
    private LocalDate day;


    public boolean validForm() {
        if(startHour == null || endHour == null)
            return false;
        return startHour < endHour;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}
