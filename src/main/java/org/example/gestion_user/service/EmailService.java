package org.example.gestion_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordEmail(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Votre mot de passe pour accéder au système");
        message.setText("Bonjour,\n\nVotre mot de passe est : " + password + "\n\nVeuillez le modifier après votre première connexion.\n\nCordialement,\nL'équipe.");
        mailSender.send(message);
    }
}
