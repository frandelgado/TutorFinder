package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Conversation;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class ConversationListDTO {

    private List<ConversationDTO> conversations;
    private int count;
    private int totalCount;

    public ConversationListDTO() {
    }

    public ConversationListDTO(final List<Conversation> conversations, final URI uri) {
        this.conversations = new LinkedList<>();
        conversations.forEach(conversation -> this.conversations.add(new ConversationDTO(conversation, uri)));
        this.count = conversations.size();
    }

    public List<ConversationDTO> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationDTO> conversations) {
        this.conversations = conversations;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
