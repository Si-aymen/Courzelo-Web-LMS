package org.example.courzelo.repositories.ProjectRepo;


import org.example.courzelo.models.ProjectEntities.project.Profileproject;
import org.example.courzelo.models.ProjectEntities.project.Speciality;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProfileprojectRepo extends MongoRepository<Profileproject,String> {


    List<Profileproject> findBySpeciality(Speciality speciality);


}
