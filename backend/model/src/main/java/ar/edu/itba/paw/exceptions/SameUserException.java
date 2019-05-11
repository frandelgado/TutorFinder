package ar.edu.itba.paw.exceptions;

public class SameUserException extends Exception {
    public SameUserException() {
    }

    public SameUserException(String message) {
        super(message);
    }
}
