package org.example.courzelo.repositories.RecruitementRepo;

import org.example.courzelo.models.RecruitementEntities.Candidacy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidacyRepo extends MongoRepository<Candidacy, String> {
}
