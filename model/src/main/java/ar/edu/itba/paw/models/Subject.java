package ar.edu.itba.paw.models;

public class Subject {
    private final Long id;
    private final String description;
    private final String name;

    public Subject(Long id, String description, String name) {
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
    //TODO: Deberia incluir la referencia a un area o consultar a un DAO con el subject para que me de el area asociada?
}
