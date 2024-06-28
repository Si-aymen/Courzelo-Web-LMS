package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.User;
import org.example.courzelo.services.IMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements IMailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendConfirmationEmail(User user, CodeVerification codeVerification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Registration Confirmation");
        mailMessage.setText("Thank you for registering!");

        mailSender.send(mailMessage);
    }
}
