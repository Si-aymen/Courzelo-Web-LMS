package org.example.courzelo.serviceImpls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.User;
import org.example.courzelo.services.IMailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements IMailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendConfirmationEmail(User user, CodeVerification codeVerification) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                + "<p>Thank you for registering. Please click the below link to verify your email:</p>"
                + "<a href='http://localhost:4200/sessions/verify-email?code=" + codeVerification.getCode() + "'>Verify Email</a>"
                + "<p>If you did not make this request, you can ignore this email.</p>"
                + "<p>Best,</p>"
                + "<p>Courzelo</p>";

        try {
            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Registration Confirmation");
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, CodeVerification codeVerification) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                + "<p>You have requested to reset your password. Please click the below link to proceed:</p>"
                + "<a href='http://localhost:4200/sessions/reset-password?code=" + codeVerification.getCode() + "'>Reset Password</a>"
                + "<p>If you did not make this request, please ignore this email.</p>"
                + "<p>Best regards,</p>"
                + "<p>Courzelo Team</p>";

        try {
            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset");
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
