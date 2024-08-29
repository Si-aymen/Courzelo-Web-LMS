package org.example.courzelo.repositories.RevisionRepo;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileMetadatarevisionRepository extends MongoRepository<FileMetadatarevision, String> {
    List<FileMetadatarevision> findFileMetadataByRevisionId (String RevisionId) ;
    FileMetadatarevision findByFileDownloadUri(String fileDownloadUri);

}


