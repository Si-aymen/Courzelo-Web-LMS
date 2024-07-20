package org.example.courzelo.repositories;

import org.example.courzelo.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByIdIn(List<String> ids);
}
