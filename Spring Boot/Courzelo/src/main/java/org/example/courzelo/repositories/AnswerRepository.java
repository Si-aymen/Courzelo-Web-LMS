package org.example.courzelo.repositories;

import org.example.courzelo.models.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface AnswerRepository extends MongoRepository<Answer, String> {
}
