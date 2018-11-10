package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {

    private static final String BASE_HIBERNATE_SELECT = "select distinct c ";

    private static final String BASE_HIBERNATE_FROM = "from Course as c join c.professor.timeslots as t ";

    private static final String BASE_HIBERNATE_WHERE = null;

    private static final String BASE_SELECT = "SELECT DISTINCT courses.user_id, courses.subject_id," +
            "courses.description, price, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id, professors.profile_picture, areas.image ";

    private static final String BASE_FROM = "FROM courses, users, subjects, areas, professors LEFT OUTER JOIN schedules " +
            "ON schedules.user_id = professors.user_id ";

    private static final String BASE_WHERE = "WHERE courses.user_id = users.user_id AND" +
            " courses.subject_id = subjects.subject_id AND professors.user_id = users.user_id " +
            "AND areas.area_id = subjects.area_id ";


    private  final String SELECT;

    private final String FROM;

    private final String WHERE;

    private final String TIME_FILTERS;

    private final List<Object> params;

    private FilterBuilder(String SELECT, String FROM, String WHERE, String TIME_FILTERS, List<Object> params) {
        this.SELECT = SELECT;
        this.FROM = FROM;
        this.WHERE = WHERE;
        this.TIME_FILTERS = TIME_FILTERS;
        this.params = params;
    }

    public FilterBuilder(){
        this.SELECT = BASE_HIBERNATE_SELECT;
        this.FROM = BASE_HIBERNATE_FROM;
        this.WHERE = BASE_HIBERNATE_WHERE;
        this.TIME_FILTERS = null;
        this.params = new ArrayList<>();
    }


    public FilterBuilder filterByTimeslot(final Integer day, final Integer startHour, final Integer endHour){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if(day != null) {
            sb.append("t.day = ");
            sb.append(day.toString());
            sb.append(" ");
            if(startHour != null || endHour != null)
                sb.append(" and ");
        }

        if(startHour != null){
            sb.append("t.hour >= ");
            sb.append(startHour.toString());
            sb.append(" ");
            if(endHour != null)
                sb.append("and ");
        }

        if(endHour != null){
            sb.append("t.hour < ");
            sb.append(endHour.toString());
        }

        if(this.TIME_FILTERS != null)
            sb.insert(0,"or ").insert(0, this.TIME_FILTERS);

        sb.append(") ");
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE, sb.toString(), this.params);

    }

    public FilterBuilder filterByPrice(final Double minPrice, final Double maxPrice){
        StringBuilder sb = new StringBuilder();
        if(minPrice != null){
            sb.append("AND c.price >= ");
            sb.append(minPrice.toString());
            sb.append(" ");
        }

        if(maxPrice != null){
            sb.append("AND c.price <= ");
            sb.append(maxPrice.toString());
            sb.append(" ");
        }
        if(this.WHERE == null){
            sb.delete(0, 3);
            sb.insert(0, "where");
            return new FilterBuilder(this.SELECT, this.FROM, sb.toString(),
                    this.TIME_FILTERS, this.params);
        }
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE + sb.toString(),
                this.TIME_FILTERS, this.params);
    }

    public FilterBuilder filterByName(String name){
        if(name != null) {
            this.params.add("%" + name + "%");
            if(this.WHERE == null) {
                return new FilterBuilder(this.SELECT, this.FROM, "where upper(c.subject.name) like upper(?) ",
                        this.TIME_FILTERS, this.params);
            }
            return new FilterBuilder(this.SELECT, this.FROM, this.WHERE + "and upper(c.subject.name) like upper(?) ",
                    this.TIME_FILTERS, this.params);
        }
        else{
            return this.clone();
        }
    }

    public Filter getFilter(){
        if(TIME_FILTERS == null){
            return new Filter(SELECT+FROM+WHERE, params);
        } else {
            if(this.WHERE == null){
                return new Filter(SELECT+FROM+ " where ( " + TIME_FILTERS + " ) ", params);
            }
            return new Filter(SELECT+FROM+WHERE+ " and ( " + TIME_FILTERS + " ) ", params);
        }
    }

    @Override
    public FilterBuilder clone(){
        return new FilterBuilder(this.SELECT, this.FROM, this.WHERE, this.TIME_FILTERS, this.params);
    }
}
