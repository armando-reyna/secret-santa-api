package com.airsoftware.secretsantaapi.service;

import com.airsoftware.secretsantaapi.model.SecretSantaGroup;
import lombok.AllArgsConstructor;
import org.antlr.stringtemplate.StringTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class CustomMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger log = LoggerFactory.getLogger(CustomMailSender.class);

    @Value("${spring.mail.cc}")
    public String MAIL_CC;

    @Value("${spring.mail.username}")
    public String MAIL_FROM;

    @Value("${spring.mail.templates}")
    public String TEMPLATES_URI;

    @Value("classpath:templates/mail/celebrate.html")
    Resource resourceFile;


    private void sendMail(String to, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(MAIL_FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
        javaMailSender.send(message);
    }

    @Async
    public void sendEmailToGroup(final SecretSantaGroup secretSantaGroup) {
        try {
            String content = isToFile(resourceFile.getInputStream());

            StringTemplate newEventTemplate = new StringTemplate(content);
            newEventTemplate.setAttribute("email", secretSantaGroup.getEmail());

            final StringBuilder message = new StringBuilder();
            message.append("<ul>");
            secretSantaGroup.getMembers().forEach(secretSantaPerson -> {
                message.append("<li>")
                        .append(secretSantaPerson.getName())
                        .append(" le regala a ")
                        .append(secretSantaPerson.getGivesTo().getName())
                        .append("</li>");
            });
            message.append("</ul>");

            newEventTemplate.setAttribute("message", message.toString());

            sendMail(secretSantaGroup.getEmail(), "Intercambio", newEventTemplate.toString());
        } catch (final Exception e) {
            log.error("The email send out failed.", e);
        }

    }

    private String isToFile(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

}
