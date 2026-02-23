package com.dd25.dietiestates25.service.utilityService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService 
{
    private JavaMailSender mailSender;
    
    @Async
    public void sendOnboardingEmail(String to, String token) 
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@dietiestates.com");
        message.setTo(to);
        message.setSubject("Benvenuto in DietiEstates - Attiva il tuo account");
        
        String link = "http://localhost:8080/link_login/" + token;
        
        message.setText("Ciao!\n\nIl tuo account è stato creato. " +
                        "Per impostare la tua password e iniziare a lavorare, clicca sul link qui sotto:\n" + 
                        link + "\n\nIl link scadrà tra 24 ore.");
        
        mailSender.send(message);
    }
    
    @Async
    public void sendPasswordResetEmail(String to, String token) 
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@dietiestates.com");
        message.setTo(to);
        message.setSubject("DietiEstates - Reimposta la tua password");

        String link = "http://localhost:8080/link_login/" + token;

        message.setText("Ciao!\n\nAbbiamo ricevuto una richiesta di reimpostazione della password per il tuo account. " +
                        "Per impostare una nuova password, clicca sul link qui sotto:\n" + 
                        link + "\n\nIl link scadrà tra 24 ore. Se non hai richiesto questa operazione, ignora questa email.");
                        
        mailSender.send(message);
    }
}