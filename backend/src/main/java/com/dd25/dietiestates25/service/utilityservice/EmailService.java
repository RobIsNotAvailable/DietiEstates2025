package com.dd25.dietiestates25.service.utilityservice;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class EmailService 
{
    private final JavaMailSender mailSender;
    
    @Async
    public void sendOnboardingEmail(String to, String token)
    {
        String link = "http://localhost:4200/link-login/" + token;
        String title = "Welcome to DietiEstates";
        String body = "Your account was created successfully. To activate it, click the button below:";
        String buttonText = "Activate my account";
        
        sendHtmlEmail(to, "Welcome to DietiEstates - Activate your account", title, body, buttonText, link);
    }

    @Async
    public void sendPasswordResetEmail(String to, String token)
    {
        String link = "http://localhost:4200/link-login/" + token;
        String title = "Password Reset Request";
        String body = "We received a request to reset your password. Click the button below to proceed:";
        String buttonText = "Reset Password";

        sendHtmlEmail(to, "DietiEstates - Reset your password", title, body, buttonText, link);
    }

    private void sendHtmlEmail(String to, String subject, String title, String body, String buttonText, String link)
    {
        try 
        {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@dietiestates.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = """
                <html>
                    <body style="font-family: Arial, sans-serif; color: #000000; line-height: 1.6;">
                        <h2 style="color: #2c3e50;">%s</h2>
                        <p>%s</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" 
                               style="background-color: #4caf50; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                               %s
                            </a>
                        </div>
                        <p style="font-size: 12px; color: #000000;">Otherwise, visit this link: %s</p>
                        <p>The link will expire in 24 hours.</p>
                        <hr style="border: 0; border-top: 1px solid #f3f3f3; margin: 20px 0;">
                    </body>
                </html>
            """.formatted(title, body, link, buttonText, link);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e)
        {
            throw new RuntimeException("Failed to send email to " + to + " with subject: " + subject, e);
        }
    }
}