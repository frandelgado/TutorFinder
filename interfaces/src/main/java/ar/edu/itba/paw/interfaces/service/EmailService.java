package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

    public void sendMailToProfessor(Professor professor, String subject, String text);

    void sendRegistrationEmail(String email);

    void sendRegistrationEmail(User user);

    void sendContactEmail(User from, User to, Conversation conversation);
}
