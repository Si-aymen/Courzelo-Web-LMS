package org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo;

import org.example.courzelo.models.RevisionEntities.QizzRevision.QuestionRevision;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRevisionRepository extends MongoRepository<QuestionRevision, String> {
    List<QuestionRevision> findByQuizRevisionId(String quizRevisionId);
}
