package org.example.courzelo.controllers.Project;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.ProjectEntities.FileMetadata;
import org.example.courzelo.repositories.ProjectRepo.FileMetadataRepository;
import org.example.courzelo.services.Project.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;


@RestController

@Tag(name = "pdf")
public class pdfControoller {
    private final PdfService pdfService;
    private final ServletContext servletContext;

    @Autowired
    public pdfControoller(PdfService pdfService, ServletContext servletContext) {
        this.pdfService = pdfService;
        this.servletContext = servletContext;
    }

    @PostMapping("/upload")
    public FileMetadata uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("projectId") String projectId) {
        try {
            return pdfService.storeFile(file, projectId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file. Please try again!", ex);
        }
    }

    @PostMapping("/project/{projectId}/upload")
    public FileMetadata upload(@RequestParam("file") MultipartFile file, @PathVariable String projectId) {
        try {
            return pdfService.storeFile(file, projectId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file. Please try again!", ex);
        }
    }

    @GetMapping("/project/{projectId}")
    public List<FileMetadata> getFilesByProjectId(@PathVariable String projectId) {
        return pdfService.getFilesByProjectId(projectId);
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws IOException {
        Resource resource = pdfService.loadFileAsResource(fileName);

        String mimeType = servletContext.getMimeType(resource.getFile().getAbsolutePath());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Default MIME type
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}