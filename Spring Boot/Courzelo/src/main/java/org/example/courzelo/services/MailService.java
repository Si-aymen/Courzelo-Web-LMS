package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.MailExchange;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.MailExchangeRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.IMailExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService implements IMailExchange {
    @Autowired
    private MailExchangeRepository mailExchangeRepository;
    private final UserRepository userRepository;
    @Override
    public MailExchange saveMail(MailExchange mailExchange) {
        return mailExchangeRepository.save(mailExchange);
    }

    @Override
    public List<MailExchange> getAllMails() {
        return mailExchangeRepository.findAll();
    }

    @Override
    public List<MailExchange> getMailsByRecipient(String recipientId) {
        return mailExchangeRepository.findByRecipientId(recipientId);
    }
    public List<MailExchange> getMailsBySenderEmail1(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return mailExchangeRepository.findBySenderId(user.getId());
        } else {
            return List.of(); // Or throw an exception if appropriate
        }
    }

    public List<MailExchange> getMailsByRecipientEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return mailExchangeRepository.findByRecipientId(user.getId());
        } else {
            return List.of(); // Or throw an exception if appropriate
        }
    }
    @Override
    public List<MailExchange> getMailsBySender(String senderId) {
        return mailExchangeRepository.findBySenderId(senderId);
    }
    @Transactional
    public boolean deleteMail(String id) {
        try {
            if (mailExchangeRepository.existsById(id)) {
                mailExchangeRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            // Log the error if necessary
            return false;
        }
    }

}
