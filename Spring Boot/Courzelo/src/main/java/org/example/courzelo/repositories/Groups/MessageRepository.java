package org.example.courzelo.repositories.Groups;

import org.example.courzelo.models.GroupChat.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByGroupId(String groupId);
}