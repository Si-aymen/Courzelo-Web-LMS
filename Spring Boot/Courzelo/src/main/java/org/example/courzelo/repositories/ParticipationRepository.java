package org.example.courzelo.repositories;

import org.example.courzelo.models.Participation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParticipationRepository extends MongoRepository<Participation, String> {
    List<Participation> findByStudentId(String studentId);
}
