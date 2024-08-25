package org.example.courzelo.repositories;

import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends MongoRepository<Institution, String> {
    Optional<Institution> findByName(String name);

}
