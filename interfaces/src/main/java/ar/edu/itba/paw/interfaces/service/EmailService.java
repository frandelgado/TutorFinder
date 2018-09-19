package ar.edu.itba.paw.interfaces.service;

public interface EmailService {

    public void sendMessage(String to, String subject, String text);
}
