package com.smarttodo.dto.email;

import com.smarttodo.model.User;
import com.smarttodo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by kpfromer on 4/25/17.
 */
//todo: create test
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private TokenService service;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    //todo: add all the values to the application.properties file
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String senderAddress = "test@gmail.com";
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(senderAddress);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        String message = "Please click on the following link to register account!";
        email.setText(message + "\nhttp://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}

