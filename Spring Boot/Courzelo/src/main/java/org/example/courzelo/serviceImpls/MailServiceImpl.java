package org.example.courzelo.serviceImpls;


import lombok.AllArgsConstructor;
import org.example.courzelo.configurations.RabbitProducerConfig;
import org.example.courzelo.dto.requests.EmailRequest;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.models.User;
import org.example.courzelo.services.IMailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements IMailService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void sendConfirmationEmail(User user, CodeVerification codeVerification) {

        String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                + "<p>Thank you for registering. Please click the below link to verify your email:</p>"
                + "<a href='http://localhost:4200/sessions/verify-email?code=" + codeVerification.getCode() + "'>Verify Email</a>"
                + "<p>If you did not make this request, you can ignore this email.</p>"
                + "<p>Best,</p>"
                + "<p>Courzelo</p>";
        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .subject("Email Verification")
                .text(htmlMsg)
                .build();
        rabbitTemplate.convertAndSend(RabbitProducerConfig.EXCHANGE, RabbitProducerConfig.ROUTING_KEY, emailRequest);
    }

    @Override
    public void sendPasswordResetEmail(User user, CodeVerification codeVerification) {

        String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                + "<p>You have requested to reset your password. Please click the below link to proceed:</p>"
                + "<a href='http://localhost:4200/sessions/reset-password?code=" + codeVerification.getCode() + "'>Reset Password</a>"
                + "<p>If you did not make this request, please ignore this email.</p>"
                + "<p>Best regards,</p>"
                + "<p>Courzelo Team</p>";

            EmailRequest emailRequest = EmailRequest.builder()
                    .to(user.getEmail())
                    .subject("Password Reset")
                    .text(htmlMsg)
                    .build();
        rabbitTemplate.convertAndSend(RabbitProducerConfig.EXCHANGE, RabbitProducerConfig.ROUTING_KEY, emailRequest);
    }

    @Override
    public void sendInstituionInvitationEmail(User user, Institution instituion, CodeVerification codeVerification) {

            String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                    + "<p>You have been invited to join " + instituion.getName() + " on Courzelo. Please click the below link to proceed:</p>"
                    + "<a href='http://localhost:4200/institution/"+instituion.getId()+"?code=" + codeVerification.getCode() + "'>Join " + instituion.getName() + "</a>"
                    + "<p>If you did not make this request, please ignore this email.</p>"
                    + "<p>Best regards,</p>"
                    + "<p>Courzelo Team</p>";

            EmailRequest emailRequest = EmailRequest.builder()
                    .to(user.getEmail())
                    .subject(instituion.getName()+" Invitation")
                    .text(htmlMsg)
                    .build();
            rabbitTemplate.convertAndSend(RabbitProducerConfig.EXCHANGE, RabbitProducerConfig.ROUTING_KEY, emailRequest);
    }

    @Override
    public void sendInstituionInvitationEmail(String email, Institution instituion, CodeVerification codeVerification) {
        String htmlMsg = "<h3>Hello, " + email + "</h3>"
                + "<p>You have been invited to join " + instituion.getName() + " on Courzelo. Please click the below link to proceed:</p>"
                + "<a href='http://localhost:4200/institution/"+instituion.getId()+"?code=" + codeVerification.getCode() + "'>Join " + instituion.getName() + "</a>"
                + "<p>If you did not make this request, please ignore this email.</p>"
                + "<p>Best regards,</p>"
                + "<p>Courzelo Team</p>";
        EmailRequest emailRequest = EmailRequest.builder()
                .to(email)
                .subject(instituion.getName()+" Invitation")
                .text(htmlMsg)
                .build();
        rabbitTemplate.convertAndSend(RabbitProducerConfig.EXCHANGE, RabbitProducerConfig.ROUTING_KEY, emailRequest);
    }
}
