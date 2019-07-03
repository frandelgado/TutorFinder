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
    private long totalCount;

    public ConversationListDTO() {
    }

    public ConversationListDTO(final List<Conversation> conversations, final long totalCount, final URI uri) {
        this.conversations = new LinkedList<>();
        conversations.forEach(conversation -> this.conversations.add(new ConversationDTO(conversation, uri)));
        this.count = conversations.size();
        this.totalCount = totalCount;
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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
