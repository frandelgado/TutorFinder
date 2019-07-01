package ar.edu.itba.paw.models;

import java.util.List;

public class PagedResults<T> {

    private final List<T> results;
    private final boolean hasNext;

    private final long total;
    private final int page;
    private final int lastPage;

    public PagedResults(List<T> results, boolean hasNext) {
        this.results = results;
        this.hasNext = hasNext;

        total = 0;
        page = 0;
        lastPage = 0;
    }

    public PagedResults(final List<T> results, final long total, final int page, final int lastPage) {
        this.results = results;
        this.hasNext = page < lastPage;
        this.total = total;
        this.page = page;
        this.lastPage = lastPage;
    }

    public List<T> getResults() {
        return results;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getLastPage() {
        return lastPage;
    }
}
