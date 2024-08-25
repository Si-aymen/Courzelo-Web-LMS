package org.example.mailing;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailListener {
    @Autowired
    private MailService mailService;

    @RabbitListener(queues = RabbitConsumerConfig.QUEUE)
    public void receiveMessage(EmailRequest emailRequest) {
        mailService.sendMail(emailRequest);
    }
}
