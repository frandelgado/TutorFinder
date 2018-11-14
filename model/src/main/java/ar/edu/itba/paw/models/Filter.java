package ar.edu.itba.paw.models;

import java.util.List;

/**
 * No se puede cambiar a generic el tipo de los parametros porque algunos son de tipo Double, otros String
 */

public class Filter {

    final String query;
    final List<Object> queryParams;

    public Filter(String query, List<Object> queryParams) {
        this.query = query;
        this.queryParams = queryParams;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getQueryParams() {
        return queryParams;
    }
}
