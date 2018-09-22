package ar.edu.itba.exceptions;

public class NonexistentConversationException extends Exception {
    public NonexistentConversationException() {
    }

    public NonexistentConversationException(String message) {
        super(message);
    }
}
