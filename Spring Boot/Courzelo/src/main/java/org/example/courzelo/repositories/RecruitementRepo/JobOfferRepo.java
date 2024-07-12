package org.example.courzelo.repositories.RecruitementRepo;

import org.example.courzelo.models.RecruitementEntities.JobOffer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepo extends MongoRepository<JobOffer, String>{
    JobOffer findByTitle(String title);
}
