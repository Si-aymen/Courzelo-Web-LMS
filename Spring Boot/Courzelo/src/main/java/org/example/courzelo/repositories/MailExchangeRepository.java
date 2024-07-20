package org.example.courzelo.repositories;

import org.example.courzelo.models.MailExchange;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MailExchangeRepository extends MongoRepository<MailExchange, String> {

    // Finds mails by sender ID
    List<MailExchange> findBySenderId(String senderId);

    // Finds mails by recipient ID
    List<MailExchange> findByRecipientId(String recipientId);

    // Finds mails by sender email address
}