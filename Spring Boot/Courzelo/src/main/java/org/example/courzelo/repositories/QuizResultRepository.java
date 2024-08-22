package org.example.courzelo.repositories;

import org.example.courzelo.models.QuizResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizResultRepository extends MongoRepository<QuizResult, String> {
    List<QuizResult> findByStudentId(String studentId);
}
