package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Message;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class MessageListDTO {

    private List<MessageDTO> messages;
    private int count;

    public MessageListDTO() {
    }

    public MessageListDTO(final List<Message> messages, final URI uri) {
        this.messages = new LinkedList<>();
        for (Message message: messages) {
            this.messages.add(new MessageDTO(message, uri));
        }
        this.count = messages.size();
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
