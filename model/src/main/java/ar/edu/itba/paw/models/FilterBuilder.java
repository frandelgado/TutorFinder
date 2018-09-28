package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {


    private static final String BASE_SELECT = "SELECT DISTINCT courses.user_id, courses.subject_id," +
            "courses.description, price, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id ";

    private static final String BASE_FROM = "FROM courses, professors, users, subjects, areas, schedules ";

    private static final String BASE_WHERE = "WHERE courses.user_id = users.user_id AND" +
            " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
            "AND areas.area_id = subjects.area_id AND schedules.user_id = professors.user_id ";


    private  final String SELECT;

    private final String FROM;

    private final String WHERE;

    private final String TIME_FILTERS;

    private final List<Object> params;

    private final List<Object> timeslotParams;

    private FilterBuilder(String SELECT, String FROM, String WHERE, String TIME_FILTERS, List<Object> params, List<Object> timeslotParams) {
        this.SELECT = SELECT;
        this.FROM = FROM;
        this.WHERE = WHERE;
        this.TIME_FILTERS = TIME_FILTERS;
        this.params = params;
        this.timeslotParams = timeslotParams;
    }

    public FilterBuilder(){
        this.SELECT = BASE_SELECT;
        this.FROM = BASE_FROM;
        this.WHERE = BASE_WHERE;
        this.TIME_FILTERS = null;
        this.params = new ArrayList<>();
        this.timeslotParams = new ArrayList<>();
    }

    public FilterBuilder filterByProfessor(final long professor_id){

        this.params.add(professor_id);

        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE+"AND professors.user_id = ? ",
                this.TIME_FILTERS, this.params, this.timeslotParams);
    }

    public FilterBuilder filterByTimeslot(final int day, final int startHour, final int endHour){
        this.timeslotParams.add(day);
        this.timeslotParams.add(startHour);
        this.timeslotParams.add(endHour);
        if(this.TIME_FILTERS == null) {
            return new FilterBuilder(this.SELECT, this.FROM, this.WHERE,
                    "(schedules.day = ? AND schedules.hour >= ? AND schedules.hour < ?) ", this.params, this.timeslotParams);
        } else {
            return new FilterBuilder(this.SELECT, this.FROM, this.WHERE,
                    this.TIME_FILTERS + "OR (schedules.day = ? AND schedules.hour >= ? AND schedules.hour < ?) ",
                    this.params, this.timeslotParams);
        }

    }

    public FilterBuilder filterByPrice(final double minPrice, final double maxPrice){
        this.params.add(minPrice);
        this.params.add(maxPrice);
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE + "AND courses.price >= ? AND courses.price <= ? ",
                this.TIME_FILTERS, this.params, this.timeslotParams);
    }

    public FilterBuilder filterByName(String name){
        if(name != null) {
            this.params.add("%" + name + "%");
            return new FilterBuilder(this.SELECT, this.FROM, this.WHERE + "AND UPPER(subjects.name) LIKE UPPER(?) ",
                    this.TIME_FILTERS, this.params, this.timeslotParams);
        }
        else{
            return this.clone();
        }
    }

    public Filter getFilter(){
        if(TIME_FILTERS == null){
            return new Filter(SELECT+FROM+WHERE, params);
        } else {
            params.addAll(timeslotParams);
            return new Filter(SELECT+FROM+WHERE+ " AND ( " + TIME_FILTERS + " ) ", params);
        }
    }

    @Override
    public FilterBuilder clone(){
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE, this.TIME_FILTERS, this.params, this.timeslotParams);
    }
}
