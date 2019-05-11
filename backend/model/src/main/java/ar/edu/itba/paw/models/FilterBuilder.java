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


    private final String select;

    private final String from;

    private final String where;

    private final String timeFilters;

    private final List<Object> params;

    private FilterBuilder(String select, String from, String where, String timeFilters, List<Object> params) {
        this.select = select;
        this.from = from;
        this.where = where;
        this.timeFilters = timeFilters;
        this.params = params;
    }

    public FilterBuilder(){
        this.select = BASE_HIBERNATE_SELECT;
        this.from = BASE_HIBERNATE_FROM;
        this.where = BASE_HIBERNATE_WHERE;
        this.timeFilters = null;
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

        if(this.timeFilters != null)
            sb.insert(0,"or ").insert(0, this.timeFilters);

        sb.append(") ");
        return new FilterBuilder(this.select, this.from, this.where, sb.toString(), this.params);

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
        if(this.where == null){
            sb.delete(0, 3);
            sb.insert(0, "where");
            return new FilterBuilder(this.select, this.from, sb.toString(),
                    this.timeFilters, this.params);
        }
        return new FilterBuilder(this.select, this.from, this.where + sb.toString(),
                this.timeFilters, this.params);
    }

    public FilterBuilder filterByName(String name){
        if(name != null) {
            this.params.add("%" + name + "%");
            if(this.where == null) {
                return new FilterBuilder(this.select, this.from, "where upper(c.subject.name) like upper(?) ",
                        this.timeFilters, this.params);
            }
            return new FilterBuilder(this.select, this.from, this.where + "and upper(c.subject.name) like upper(?) ",
                    this.timeFilters, this.params);
        }
        else{
            return this.clone();
        }
    }

    public Filter getFilter(){
        if(timeFilters == null){
            if(this.where == null) {
                return new Filter(select + from, params);
            } else {
                return new Filter(select + from + where, params);
            }
        } else {
            if(this.where == null){
                return new Filter(select + from + " where ( " + timeFilters + " ) ", params);
            }
            return new Filter(select + from + where + " and ( " + timeFilters + " ) ", params);
        }
    }

    @Override
    public FilterBuilder clone(){
        return new FilterBuilder(this.select, this.from, this.where, this.timeFilters, this.params);
    }
}
