package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.Conversation;
import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

//TODO: Check latestMessage type
@XmlRootElement
public class ConversationDTO {

    private long id;
//    private UserDTO user;
//    private ProfessorDTO professor;
//    private SubjectDTO subject;

    @XmlElement(name = "messages_url")
    private URI messagesUrl;

    @XmlElement(name = "latest_message")
    private LocalDateTime latestMessage;

    public ConversationDTO() {
    }

    public ConversationDTO(final Conversation conversation, final URI baseUri) {
        this.id = conversation.getId();
        this.latestMessage = conversation.getLatestMessage();
        this.messagesUrl = baseUri.resolve("conversations/" + id +"/messages");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public URI getMessagesUrl() {
        return messagesUrl;
    }

    public void setMessagesUrl(URI messagesUrl) {
        this.messagesUrl = messagesUrl;
    }

    public LocalDateTime getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(LocalDateTime latestMessage) {
        this.latestMessage = latestMessage;
    }
}
