package com.smarttodo.dto.email;

import com.smarttodo.model.User;
import com.smarttodo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by kpfromer on 4/25/17.
 */
//todo: create test
@Component
@PropertySource("application.properties")
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private TokenService service;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${smarttodo.mail.from}")
    private String from;
    @Value("${smarttodo.mail.subject}")
    private String subject;
    @Value("${smarttodo.mail.body}")
    private String body;
    @Value("${smarttodo.mail.url}")
    private String url;

    @Override
    @Async
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String confirmationUrl = url + "/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(from);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(String.format(body, confirmationUrl));
        mailSender.send(email);
    }
}

