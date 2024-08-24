package org.example.courzelo.services.Project;

import org.example.courzelo.models.ProjectEntities.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PdfService   {
    FileMetadata storeFile(MultipartFile file, String projectId);
    List<FileMetadata> getFilesByProjectId(String projectId) ;
    Resource loadFileAsResource(String fileName);
}
