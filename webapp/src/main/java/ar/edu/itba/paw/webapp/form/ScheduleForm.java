package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Timeslot;

import javax.validation.constraints.*;

public class ScheduleForm {

    @NotNull
    @Min(1)
    @Max(7)
    private Integer day;

    @NotNull
    @Min(0)
    @Max(24)
    private Integer startHour;

    @NotNull
    @Min(0)
    @Max(24)
    private Integer endHour;

    public boolean validForm() {
        if(startHour == null || endHour == null)
            return false;
        return startHour < endHour;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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
}
