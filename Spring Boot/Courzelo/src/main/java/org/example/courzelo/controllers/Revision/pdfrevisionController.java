package org.example.courzelo.controllers.Revision;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletContext;

import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.services.Revision.PdfrevisionService;
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
@Tag(name = "pdfrevision")
public class pdfrevisionController {
    private final PdfrevisionService pdfreService;
    private final ServletContext servletContext;

    @Autowired
    public pdfrevisionController(PdfrevisionService pdfService, ServletContext servletContext) {
        this.pdfreService = pdfService;
        this.servletContext = servletContext;
    }

    @PostMapping("/uploads")
    public FileMetadatarevision uploadFiles(@RequestParam("file") MultipartFile file, @RequestParam("revisionId") String revisionId) {
        try {
            return pdfreService.storeFile(file, revisionId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file. Please try again!", ex);
        }
    }

    @PostMapping("/consultrevision/{revisionId}/uploads")
    public FileMetadatarevision uploads(@RequestParam("file") MultipartFile file, @PathVariable String revisionId) {
        try {
            return pdfreService.storeFile(file, revisionId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file. Please try again!", ex);
        }
    }

    @GetMapping("/consultrevision/{revisionId}")
    public List<FileMetadatarevision> getFilesByrevisionId(@PathVariable String revisionId) {
        return pdfreService.getFilesByrevisionId(revisionId);
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Resource resource = pdfreService.loadFileAsResource(fileName);
            String mimeType = servletContext.getMimeType(resource.getFile().getAbsolutePath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Default MIME type
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve file. Please try again!", ex);
        }
    }
}