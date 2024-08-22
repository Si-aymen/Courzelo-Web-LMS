package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepo extends MongoRepository<Project,String> {

   // List<Project> findProjectsByUsersIn(Collection<SecurityProperties.User> users);

 Project getById (String id);
}
