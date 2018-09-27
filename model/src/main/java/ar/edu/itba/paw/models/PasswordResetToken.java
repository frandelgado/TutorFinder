package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

public class PasswordResetToken {

    private final Long id;
    private final User user;
    private final String token;
    private final LocalDateTime expireDate;

    public PasswordResetToken(Long id, User user, String token, LocalDateTime expireDate) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expireDate = expireDate;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }
}
