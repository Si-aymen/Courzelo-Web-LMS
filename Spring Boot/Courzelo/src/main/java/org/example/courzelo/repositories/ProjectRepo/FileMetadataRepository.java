package org.example.courzelo.repositories.ProjectRepo;

import org.example.courzelo.models.ProjectEntities.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    List<FileMetadata> findFileMetadataByProjectId (String projectId) ;
}


