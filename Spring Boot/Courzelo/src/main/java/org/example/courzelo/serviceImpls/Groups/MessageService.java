package org.example.courzelo.serviceImpls.Groups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.GroupChat.Message;
import org.example.courzelo.repositories.Groups.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    public List<Message> getMessagesByGroupId(String groupId) {
        return messageRepository.findByGroupId(groupId);
    }

    public Message sendMessage(Message message) {
        System.out.println("Saving message: " + message);
        return messageRepository.save(message);
    }
}