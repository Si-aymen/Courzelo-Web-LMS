package org.example.courzelo.repositories.Application;

import org.example.courzelo.models.Application.Interview;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewRepository extends MongoRepository<Interview,String> {
    List<Interview> findByInterviewer(User interviewer);
}
