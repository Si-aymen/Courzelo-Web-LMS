package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.publication.Publication;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublicationRepository extends MongoRepository<Publication,String> {

    @Override
    List<Publication> findAll(Sort sort);

  List  <Publication> findByProjectId(String projectId);


}
