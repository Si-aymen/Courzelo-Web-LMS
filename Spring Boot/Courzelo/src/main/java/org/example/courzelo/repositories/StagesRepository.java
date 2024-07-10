package org.example.courzelo.repositories;

import org.example.courzelo.models.Stages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StagesRepository extends MongoRepository<Stages, String> {
}
