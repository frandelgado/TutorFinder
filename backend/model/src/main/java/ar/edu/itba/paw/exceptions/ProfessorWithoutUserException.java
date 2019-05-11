package ar.edu.itba.paw.exceptions;

public class ProfessorWithoutUserException extends Exception {
    public ProfessorWithoutUserException(String s) {
        super(s);
    }
    public ProfessorWithoutUserException(){
        super();
    }
}
