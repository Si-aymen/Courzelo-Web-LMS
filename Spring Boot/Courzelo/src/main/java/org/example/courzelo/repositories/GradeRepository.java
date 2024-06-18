package org.example.courzelo.repositories;

import org.example.courzelo.models.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GradeRepository extends MongoRepository<Grade, String> {
    List<Grade> findByEvaluationId(String evaluationId);
}
