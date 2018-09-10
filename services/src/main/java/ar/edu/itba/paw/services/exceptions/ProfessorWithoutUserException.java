package ar.edu.itba.paw.services.exceptions;

public class ProfessorWithoutUserException extends RuntimeException {
    public ProfessorWithoutUserException(String s) {
        super(s);
    }
    public ProfessorWithoutUserException(){
        super();
    }
}
