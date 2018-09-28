package ar.edu.itba.paw.models;

import java.util.List;

public class Filter {

    final String query;
    final List<Object> qeryParams;

    public Filter(String query, List<Object> qeryParams) {
        this.query = query;
        this.qeryParams = qeryParams;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getQeryParams() {
        return qeryParams;
    }
}
