package org.example.courzelo.repositories;

import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.models.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizRepository extends MongoRepository<Quiz, String> {
}
