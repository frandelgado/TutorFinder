package ar.edu.itba.paw.models;

import java.util.List;

public class PagedResults<T> {

    private final List<T> results;
    private final boolean hasNext;

    public PagedResults(List<T> results, boolean hasNext) {
        this.results = results;
        this.hasNext = hasNext;
    }

    public List<T> getResults() {
        return results;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
