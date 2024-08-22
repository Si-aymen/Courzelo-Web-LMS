package org.example.courzelo.repositories.ProjectRepo;


import org.example.courzelo.models.ProjectEntities.Profileproject;
import org.example.courzelo.models.ProjectEntities.Project;
import org.example.courzelo.models.ProjectEntities.Roleproject;
import org.example.courzelo.models.ProjectEntities.Speciality;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProfileprojectRepo extends MongoRepository<Profileproject,String> {


    List<Profileproject> findBySpeciality(Speciality speciality);


}
