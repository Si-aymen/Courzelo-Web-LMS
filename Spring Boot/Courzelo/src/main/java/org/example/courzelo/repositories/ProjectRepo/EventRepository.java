package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.Event;
import org.example.courzelo.models.ProjectEntities.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByProjectId(String projectId);
}
