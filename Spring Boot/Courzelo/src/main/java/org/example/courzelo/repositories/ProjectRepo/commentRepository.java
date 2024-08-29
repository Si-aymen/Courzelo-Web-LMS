package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.publication.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface commentRepository  extends MongoRepository<Comment,String> {
    List<Comment> findByPublicationId(String publicationId);
}
