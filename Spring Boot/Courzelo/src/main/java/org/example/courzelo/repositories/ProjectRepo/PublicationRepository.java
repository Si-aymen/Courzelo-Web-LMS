package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.Publication;
import org.example.courzelo.models.UserProfile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends MongoRepository<Publication,String> {

    @Override
    List<Publication> findAll(Sort sort);

  List  <Publication> findByProjectId(String projectId);


}
