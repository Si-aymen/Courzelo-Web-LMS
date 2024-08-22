package org.example.courzelo.serviceImpls.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.ProjectEntities.project.FileMetadata;
import org.example.courzelo.models.ProjectEntities.project.Project;
import org.example.courzelo.repositories.ProjectRepo.FileMetadataRepository;
import org.example.courzelo.repositories.ProjectRepo.ProjectRepo;
import org.example.courzelo.services.Project.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
@Service
@Slf4j
@RequiredArgsConstructor
public class pdfServiceImpl implements PdfService {

        private final Path fileStorageLocation;
        private final FileMetadataRepository fileMetadataRepository;
        private final ProjectRepo projectRepository;

        @Autowired
        public pdfServiceImpl(FileMetadataRepository fileMetadataRepository, ProjectRepo projectRepository) {
            this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
            this.fileMetadataRepository = fileMetadataRepository;
            this.projectRepository = projectRepository;

            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }

        @Override
        public FileMetadata storeFile(MultipartFile file, String projectId) {
            String fileName = file.getOriginalFilename();

            try {
                if (fileName.contains("..")) {
                    throw new RuntimeException("Invalid path sequence " + fileName);
                }

                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String fileDownloadUri = fileName;

                FileMetadata fileMetadata = new FileMetadata();
                fileMetadata.setFileName(fileName);
                fileMetadata.setFileDownloadUri(fileDownloadUri);
                fileMetadata.setProjectId(projectId);

                fileMetadataRepository.save(fileMetadata);

                Optional<Project> projectOptional = projectRepository.findById(projectId);
                if (projectOptional.isPresent()) {
                    Project project = projectOptional.get();
                    List<FileMetadata> files = project.getFiles();
                    files.add(fileMetadata);
                    project.setFiles(files);
                    projectRepository.save(project);
                }

                return fileMetadata;

            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }

        @Override
        public Resource loadFileAsResource(String fileName) {
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new RuntimeException("File not found " + fileName);
                }
            } catch (MalformedURLException ex) {
                throw new RuntimeException("File not found " + fileName, ex);
            }
        }

        @Override
        public List<FileMetadata> getFilesByProjectId(String projectId) {
            return fileMetadataRepository.findFileMetadataByProjectId(projectId);
        }
    }