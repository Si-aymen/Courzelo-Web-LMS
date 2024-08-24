package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.Project;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Collection;
import java.util.List;

public interface ProjectRepo extends MongoRepository<Project,String> {

   // List<Project> findProjectsByUsersIn(Collection<SecurityProperties.User> users);

 Project getById (String id);
}
