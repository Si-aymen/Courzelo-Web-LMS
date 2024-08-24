package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.Project;
import org.example.courzelo.models.ProjectEntities.Status;
import org.example.courzelo.models.ProjectEntities.Tasks;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface TasksRepo extends MongoRepository<Tasks,String> {
    List<Tasks> findTasksByProject (Project project);

    List<Tasks> findByProjectId(String projectId);
    List<Tasks> findByStatus(Status status);
}
