package org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo;

import org.example.courzelo.models.RevisionEntities.QizzRevision.AnswerRevision;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerRevisionRepository extends MongoRepository<AnswerRevision, String> {
}
