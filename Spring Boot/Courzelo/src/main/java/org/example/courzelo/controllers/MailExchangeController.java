package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.MailREQ;
import org.example.courzelo.models.MailExchange;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mails")
@RequiredArgsConstructor
@Slf4j
public class MailExchangeController {
    @Autowired
    private MailService mailService;
    private final UserRepository userRepository;


    @PostMapping
    public ResponseEntity<MailExchange> sendMail(@RequestBody MailREQ mailDTO) {
        // Convert DTO to entity if needed, then save
        MailExchange mail = new MailExchange();
        mail.setSubject(mailDTO.getSubject());
        mail.setDetails(mailDTO.getDetails());
        User sender = userRepository.findUserByEmail(mailDTO.getSender());
        User recipient = userRepository.findUserByEmail(mailDTO.getRecipient());

        mail.setSender(sender);
        mail.setRecipient(recipient);
        mail.setDateCreation(mailDTO.getDateCreation() != null ? mailDTO.getDateCreation() : LocalDateTime.now());

        return ResponseEntity.ok(mailService.saveMail(mail));
    }
    @GetMapping
    public ResponseEntity<List<MailExchange>> getAllMails() {
        List<MailExchange> mails = mailService.getAllMails();
        return ResponseEntity.ok(mails);
    }
    @GetMapping("/sender1/{email}")
    public List<MailExchange> getMailsBySender1(@PathVariable String email) {
        return mailService.getMailsBySenderEmail1(email);
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<MailExchange>> getMailsByRecipient(@PathVariable String recipientId) {
        List<MailExchange> mails = mailService.getMailsByRecipient(recipientId);
        return ResponseEntity.ok(mails);
    }

    @GetMapping("/sender")
    public List<MailExchange> getMailsBySender(@RequestParam String email) {
        return mailService.getMailsBySenderEmail1(email);
    }

    @GetMapping("/recipient")
    public List<MailExchange> getMailsByRecipint(@RequestParam String email) {
        return mailService.getMailsByRecipientEmail(email);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMail(@PathVariable String id) {
        boolean isDeleted = mailService.deleteMail(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // HTTP 404 Not Found
        }
    }
}

