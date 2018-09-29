package ar.edu.itba.paw.webapp.form;


import javax.validation.constraints.Min;

public class SearchForm {


    private String search;

    private String type;

    @Min(0)
    private Double minPrice;

    private Double maxPrice;

    private Integer day;
    private Integer startHour;
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

    public void setDay(Integer day) {
        this.day = day;
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

    public Integer getDay() {
        return day;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }
}
