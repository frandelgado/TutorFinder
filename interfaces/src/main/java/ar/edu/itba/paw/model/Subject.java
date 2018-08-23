package ar.edu.itba.paw.model;

public interface Subject {
    String getDescrption();
    Long getId();
    String getName();
    //TODO: Deberia incluir la referencia a un area o consultar a un DAO con el subject para que me de el area asociada?
}
