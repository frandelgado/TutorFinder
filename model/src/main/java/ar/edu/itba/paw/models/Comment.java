package ar.edu.itba.paw.models;

import org.hibernate.annotations.ColumnDefault;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_comment_id_seq")
    @SequenceGenerator(sequenceName = "comments_comment_id_seq",
            name = "comments_comment_id_seq",  allocationSize = 1)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Course course;

    @Column(nullable = false, length = 1024, name = "comment")
    private String comment;

    @Column(name = "created", nullable = false)
    @ColumnDefault("now()")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime created;

    Comment() {}

    public Comment(final User user, final Course course, final String comment, LocalDateTime created) {
        this.user = user;
        this.course = course;
        this.comment = comment;
        this.created = created;
    }

    public Comment(final Long id, final User user, final Course course, final String comment, LocalDateTime created) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.comment = comment;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getHours() {
        return String.format("%02d", created.getHourOfDay());
    }

    public String getMinutes() {
        return String.format("%02d", created.getMinuteOfHour());
    }

    public String getDay() {
        return String.format("%02d", created.getDayOfMonth());
    }

    public String getMonth() {
        return String.format("%02d", created.getMonthOfYear());
    }

    public int getYear() {
        return created.getYear();
    }
}
