package ar.edu.itba.paw.models;

public class Subject {
    private final Long id;
    private final String description;
    private final String name;
    private final Area area;

    public Subject(Long id, String description, String name, Area area) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.area = area;
    }

    public String getDescription(){
        return description;
    }
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Area getArea() {
        return area;
    }
}
