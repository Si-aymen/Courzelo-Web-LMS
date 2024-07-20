package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.MailExchange;

import java.util.List;

public interface IMailExchange {

    MailExchange saveMail(MailExchange mailExchange) ;

    public List<MailExchange> getAllMails();
    List<MailExchange> getMailsByRecipient(String recipientId) ;

    List<MailExchange> getMailsBySender(String senderId) ;

    boolean deleteMail(String id);
}
