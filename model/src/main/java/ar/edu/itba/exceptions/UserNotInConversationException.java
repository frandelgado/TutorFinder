package ar.edu.itba.exceptions;

public class UserNotInConversationException extends Exception {
    public UserNotInConversationException() {
    }

    public UserNotInConversationException(String message) {
        super(message);
    }
}
