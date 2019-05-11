package ar.edu.itba.paw.models;

import java.util.List;

public class Schedule {

    private final List<Integer> monday;
    private final List<Integer> tuesday;
    private final List<Integer> wednesday;
    private final List<Integer> thursday;
    private final List<Integer> friday;
    private final List<Integer> saturday;
    private final List<Integer> sunday;

    public Schedule(List<Integer> monday, List<Integer> tuesday, List<Integer> wednesday, List<Integer> thursday, List<Integer> friday, List<Integer> saturday, List<Integer> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public List<Integer> getMonday() {
        return monday;
    }

    public List<Integer> getTuesday() {
        return tuesday;
    }

    public List<Integer> getWednesday() {
        return wednesday;
    }

    public List<Integer> getThursday() {
        return thursday;
    }

    public List<Integer> getFriday() {
        return friday;
    }

    public List<Integer> getSaturday() {
        return saturday;
    }

    public List<Integer> getSunday() {
        return sunday;
    }
}
