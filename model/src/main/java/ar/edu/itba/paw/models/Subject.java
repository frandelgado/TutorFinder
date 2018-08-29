package ar.edu.itba.paw.models;

public class Subject {
    private Long id;
    private String description;
    private String name;

    public Subject(Long id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    String getDescrption(){
        return description;
    }
    Long getId(){
        return id;
    }
    String getName(){
        return name;
    }
    //TODO: Deberia incluir la referencia a un area o consultar a un DAO con el subject para que me de el area asociada?
}
