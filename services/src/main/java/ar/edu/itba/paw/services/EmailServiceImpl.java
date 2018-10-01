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
import org.springframework.scheduling.annotation.Async;
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

    @Value("classpath:RestorePassword.html")
    private Resource restorePassword;

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
    @Async
    public void sendRestorePasswordEmail(final User user, final String token) {
        final Document doc;
        try {
            doc = Jsoup.parse(restorePassword.getInputStream(), "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        final Element mail = doc.select("td.mail").first();
        final Element logo = doc.select("img.logo-img").first();
        logo.attr("src", "http://localhost:8080/resources/images/logo.png");
        final Element a = doc.select("a.link").first();
        a.attr("href", "http://localhost:8080/resetPassword?token=" + token);
        mail.text("Email: " + user.getEmail());
        String SUBJECT = "Restaura tu contraseña";

        final MimeMessage message = prepareMail(SUBJECT, user.getEmail(), doc.html());

        if(message == null)
            throw new RuntimeException();

        emailSender.send(message);
    }


    @Override
    @Async
    public void sendRegistrationEmail(final User user) {
        final Document doc;
        try {
            doc = Jsoup.parse(registrationMail.getInputStream(), "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        final Element username = doc.select("td.username").first();
        final Element logo = doc.select("img.logo-img").first();
        logo.attr("src", "http://localhost:8080/resources/images/logo.png");
        final Element a = doc.select("a.link").first();
        a.attr("href", "http://localhost:8080");
        username.text("Username: " + user.getUsername());
        String REGISTRATION_SUBJECT = "Bienvenido a Tu Teoria!";

        final MimeMessage message = prepareMail(REGISTRATION_SUBJECT, user.getEmail(), doc.html());

        if(message == null)
            throw new RuntimeException();

        emailSender.send(message);
    }

    @Override
    @Async
    public void sendContactEmail(final User from, final User to, final Conversation conversation) {
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
        final Element a = doc.select("a.conversation-button").first();
        a.attr("href", "http://localhost:8080/Conversation?id=" + conversation.getId());
        final Element logo = doc.select("img.logo-img").first();
        logo.attr("src", "http://localhost:8080/resources/images/logo.png");
        final String SUBJECT = "Se han contactado con vos!";

        final MimeMessage message = prepareMail(SUBJECT, to.getEmail(), doc.html());

        if(message == null)
            throw new RuntimeException();

        emailSender.send(message);
    }

    private MimeMessage prepareMail(final String subject, final String to, final String html) {
        final MimeMessage message;
        try {
            message = emailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (MessagingException e) {
            return null;
        }
        return message;
    }

}
