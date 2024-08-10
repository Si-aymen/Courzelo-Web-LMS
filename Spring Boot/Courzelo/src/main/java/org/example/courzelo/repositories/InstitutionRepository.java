package org.example.courzelo.repositories;

import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends MongoRepository<Institution, String> {

}
