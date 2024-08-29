package org.example.courzelo.services.Revision;

import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PdfrevisionService {
    FileMetadatarevision storeFile(MultipartFile file, String revisionId);

    Resource loadFileAsResource(String fileName);
    List<FileMetadatarevision> getFilesByrevisionId(String revisionId);
}
