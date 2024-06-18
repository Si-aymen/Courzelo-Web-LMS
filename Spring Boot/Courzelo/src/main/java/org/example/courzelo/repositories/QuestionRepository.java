package org.example.courzelo.repositories;

import org.example.courzelo.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
