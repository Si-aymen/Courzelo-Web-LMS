package org.example.courzelo.repositories;

import org.example.courzelo.models.Evaluation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
}
