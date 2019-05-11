package ar.edu.itba.paw.webapp.form;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class SearchForm {


    private String search;

    private String type = "course";

    @Min(0)
    private Double minPrice;

    @Min(0)
    private Double maxPrice;

    private List<Integer> days;

    @Min(1)
    @Max(23)
    private Integer startHour;

    @Min(2)
    @Max(24)
    private Integer endHour;

    public void setSearch(String search) {
        this.search = search;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public String getSearch() {
        return search;
    }

    public String getType() {
        return type;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public boolean validTimeRange() {
        if(startHour == null || endHour == null)
            return true;
        return startHour < endHour;
    }

    public boolean validPriceRange() {
        if(maxPrice == null || minPrice == null)
            return true;
        return minPrice <= maxPrice;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }
}
