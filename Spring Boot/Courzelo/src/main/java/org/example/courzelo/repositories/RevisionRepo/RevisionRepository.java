package org.example.courzelo.repositories.RevisionRepo;

import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RevisionRepository extends MongoRepository<Revision, String> {

}