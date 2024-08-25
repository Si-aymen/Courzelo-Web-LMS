package org.example.courzelo.repositories;

import org.example.courzelo.models.Participation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ParticipationRepository extends MongoRepository<Participation, String> {
    List<Participation> findByStudent(String studentId);
}
