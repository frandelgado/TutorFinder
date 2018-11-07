package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "reset_password_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_password_tokens_id_seq")
    @SequenceGenerator(sequenceName = "reset_password_tokens_id_seq", name = "reset_password_tokens_id_seq",  allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false, length = 36)
    private String token;

    @Column(nullable = false, name = "expires")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expireDate;

    PasswordResetToken(){}

    public PasswordResetToken(User user, String token, LocalDateTime expireDate) {
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
