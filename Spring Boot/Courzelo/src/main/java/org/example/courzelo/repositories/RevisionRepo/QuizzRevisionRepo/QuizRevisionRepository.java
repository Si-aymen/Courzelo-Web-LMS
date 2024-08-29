package org.example.courzelo.repositories.RevisionRepo.QuizzRevisionRepo;

import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRevisionRepository extends MongoRepository<QuizRevision, String> {

    Optional<QuizRevision> findByTitle(String title);


    List<QuizRevision> findByRevision_Id (String revisionId);

}
