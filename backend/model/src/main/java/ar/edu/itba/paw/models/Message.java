package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_message_id_seq")
    @SequenceGenerator(sequenceName = "messages_message_id_seq",
            name = "messages_message_id_seq",  allocationSize = 1)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="sender_id", foreignKey = @ForeignKey(name = "messages_sender_id_fkey"))
    private User sender;

    @Column(nullable = false, length = 1024, name = "message")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="conversation_id", foreignKey = @ForeignKey(name = "messages_conversation_id_fkey"))
    private Conversation conversation;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime created;

    Message() {}

    public Message(User sender, String text, LocalDateTime created) {
        this.sender = sender;
        this.text = text;
        this.created = created;
    }

    public Message(Long id, User sender, String text, LocalDateTime created) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
