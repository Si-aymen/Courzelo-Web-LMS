package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.ChatProject.Chat;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat,String> {
}
