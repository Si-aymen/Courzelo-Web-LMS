package org.example.courzelo.serviceImpls.Revision;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.example.courzelo.repositories.RevisionRepo.FileMetadatarevisionRepository;
import org.example.courzelo.repositories.RevisionRepo.RevisionRepository;
import org.example.courzelo.services.Revision.PdfrevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class pdfrevisionServiceImpl implements PdfrevisionService {

    private final Path fileStorageLocation;
    private final FileMetadatarevisionRepository fileMetadatarevisionRepository;
    private final RevisionRepository revisionRepository;

    @Autowired
    public pdfrevisionServiceImpl(FileMetadatarevisionRepository fileMetadatarevisionRepository, RevisionRepository revisionRepository) {
        // Set the file storage location to a folder named "uploads" in the current directory
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        this.fileMetadatarevisionRepository = fileMetadatarevisionRepository;
        this.revisionRepository = revisionRepository;

        try {
            // Create the directory if it does not exist
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FileMetadatarevision storeFile(MultipartFile file, String revisionId) {
        String fileName = file.getOriginalFilename();

        try {
            // Check for invalid path sequence
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid path sequence " + fileName);
            }

            // Resolve the file storage location and store the file
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Generate the file download URI
            String fileDownloadUri = targetLocation.toUri().toString();

            // Save file metadata to the database
            FileMetadatarevision fileMetadatarevision = new FileMetadatarevision();
            fileMetadatarevision.setFileName(fileName);
            fileMetadatarevision.setFileDownloadUri(fileDownloadUri); // Set the correct file URI
            fileMetadatarevision.setRevisionId(revisionId);

            fileMetadatarevisionRepository.save(fileMetadatarevision);

            // Update the Revision entity with the new file
            Optional<Revision> revisionOptional = revisionRepository.findById(revisionId);
            if (revisionOptional.isPresent()) {
                Revision revision = revisionOptional.get();
                List<FileMetadatarevision> files = revision.getFiles();
                files.add(fileMetadatarevision);
                revision.setFiles(files);
                revisionRepository.save(revision);
            }

            return fileMetadatarevision;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            // Resolve the file path
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
    public List<FileMetadatarevision> getFilesByrevisionId(String revisionId) {
        return fileMetadatarevisionRepository.findFileMetadataByRevisionId(revisionId);
    }
}
