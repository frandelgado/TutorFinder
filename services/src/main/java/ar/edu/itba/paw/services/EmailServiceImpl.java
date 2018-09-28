package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("classpath:WelcomeMail.html")
    private Resource registrationMail;

    @Value("classpath:ConversationMail.html")
    private Resource contactMail;

    @Autowired
    private JavaMailSender emailSender;

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

    @Override
    public void sendRegistrationEmail(User user) {
        final Document doc;
        try {
            doc = Jsoup.parse(registrationMail.getInputStream(), "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        Element username = doc.select("td.username").first();
        username.text("Username: " + user.getUsername());
        String REGISTRATION_SUBJECT = "Bienvenido a Tu Teoria!";

        final MimeMessage message = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);

        final boolean prepared = prepareMail(REGISTRATION_SUBJECT, user.getEmail(), helper, doc.text());

        if(!prepared)
            throw new RuntimeException();

        emailSender.send(message);
    }

    @Override
    public void sendContactEmail(User from, User to, Conversation conversation) {
        final Document doc;
        try {
            doc = Jsoup.parse(contactMail.getInputStream(), "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        Element element = doc.select("td.name").first();
        element.text("Nombre: " + from.getName());
        element = doc.select("td.lastname").first();
        element.text("Apellido: " + from.getLastname());
        element = doc.select("td.subject").first();
        element.text("Materia: " + conversation.getSubject().getName());
        Element a = doc.select("a.conversation-button").first();
        a.attr("href", "http://localhost:8080/Conversation?id=" + conversation.getId());
        final String SUBJECT = "Se han contactado con vos!";

        final MimeMessage message = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);

        final boolean prepared = prepareMail(SUBJECT, to.getEmail(), helper, doc.text());
        if(!prepared)
            throw new RuntimeException();

        emailSender.send(message);
    }

    private boolean prepareMail(final String subject, final String to, final MimeMessageHelper helper, final String html) {
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

}
