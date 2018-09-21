package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

public interface ConversationService {

    public boolean sendMessage(final User user, final Professor professor, final Subject subject, final String body);

    public boolean sendMessage(final User from, final Conversation conversation, final String body);
}
