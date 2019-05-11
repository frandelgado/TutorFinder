package ar.edu.itba.paw.exceptions;

public class UsernameAndEmailAlreadyInUseException extends Exception {
    public UsernameAndEmailAlreadyInUseException() {
    }

    public UsernameAndEmailAlreadyInUseException(String message) {
        super(message);
    }
}
