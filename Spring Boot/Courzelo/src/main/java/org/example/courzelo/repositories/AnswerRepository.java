package org.example.courzelo.repositories;

import org.example.courzelo.models.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findByQuestionID(String questionId);
}
