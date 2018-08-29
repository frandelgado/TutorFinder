package ar.edu.itba.paw.models;

public class Area {

    private final Long id;
    private final String description;
    private final String name;

    public Area(Long id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public String getDescrption(){
     return description;
    }
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
