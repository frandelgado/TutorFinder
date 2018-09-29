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

    public FilterBuilder filterByTimeslot(final Integer day, final Integer startHour, final Integer endHour){
        StringBuilder sb = new StringBuilder();
        sb.append("(schedules.day = ? ");
        this.timeslotParams.add(day);

        if(startHour != null && endHour != null){
            this.timeslotParams.add(startHour);
            this.timeslotParams.add(endHour);
            sb.append("AND schedules.hour >= ? AND schedules.hour < ?");

        }
        
        if(this.TIME_FILTERS != null)
            sb.insert(0,"OR ").insert(0, this.TIME_FILTERS);

        sb.append(") ");
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE, sb.toString(), this.params, this.timeslotParams);

    }

    public FilterBuilder filterByPrice(final Double minPrice, final Double maxPrice){
        StringBuilder sb = new StringBuilder();
        if(minPrice != null){
            sb.append("AND courses.price >= ? ");
            this.params.add(minPrice);
        }

        if(maxPrice != null){
            sb.append("AND courses.price <= ? ");
            this.params.add(maxPrice);
        }

        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE + sb.toString(),
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
