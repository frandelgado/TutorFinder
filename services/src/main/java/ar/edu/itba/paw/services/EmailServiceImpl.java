package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMailToProfessor(Professor professor, String subject, String text) {
        sendEmail(professor.getEmail(), subject, text);
    }

    @Override
    public void sendRegistrationEmail(String email) {
        String REGISTRATION_SUBJECT = "Bienvenido a Tu Teoria!";
        String REGISTRATION_BODY = "Te damos la bienvenida a Tu Teoria, encuentra tu proxima clase particular de manera rapida y sencilla.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(REGISTRATION_SUBJECT);
        message.setText(REGISTRATION_BODY);
        emailSender.send(message);
    }

}
