package org.example.courzelo.repositories;

import org.example.courzelo.models.Academic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AcademicRepository extends MongoRepository<Academic, String> {
    List<Academic> findByStudentId(String studentId);
}
