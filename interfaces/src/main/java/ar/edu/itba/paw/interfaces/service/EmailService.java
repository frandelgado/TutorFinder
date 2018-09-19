package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Professor;

public interface EmailService {

    public void sendMessage(String to, String subject, String text);
    public void sendMailToProfessor(Professor professor, String subject, String text);
}
