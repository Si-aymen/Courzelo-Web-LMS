package org.example.courzelo.repositories;

import org.example.courzelo.models.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfessorRepository extends MongoRepository<Professor, String> {

}
