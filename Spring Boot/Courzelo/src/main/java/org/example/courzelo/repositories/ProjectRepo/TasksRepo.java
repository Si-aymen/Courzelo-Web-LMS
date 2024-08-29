package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.project.Project;
import org.example.courzelo.models.ProjectEntities.project.Status;
import org.example.courzelo.models.ProjectEntities.project.Tasks;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface TasksRepo extends MongoRepository<Tasks,String> {
    List<Tasks> findTasksByProject (Project project);

    List<Tasks> findByProjectId(String projectId);
    List<Tasks> findByStatus(Status status);
}
