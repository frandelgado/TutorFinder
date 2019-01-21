package ar.edu.itba.paw.exceptions;

public class NonexistentProfessorException extends Exception {
    public NonexistentProfessorException() {
    }

    public NonexistentProfessorException(String message) {
        super(message);
    }
}
