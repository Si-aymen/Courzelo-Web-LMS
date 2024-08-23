package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.project.GroupProject;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface GroupProjectRepo extends MongoRepository<GroupProject,String> {
    boolean existsByProjectId(String projectId);

    List<GroupProject> findByStudentsId(String studentId);
}