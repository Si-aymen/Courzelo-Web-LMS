package org.example.courzelo.controllers.Project;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.ProjectEntities.Comment;
import org.example.courzelo.models.ProjectEntities.Publication;
import org.example.courzelo.models.ProjectEntities.Reaction;
import org.example.courzelo.services.Project.PublicationService;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Publication")
public class PublicationController {
    private final PublicationService publicationService;

    @PostMapping("/publication/{projectId}")
    public ResponseEntity<Publication> createPublication(
            @PathVariable String projectId,
            @RequestBody Publication publication,
            Authentication authentication) {
        String email = authentication.getName();
        Publication createdPublication = publicationService.createPublication(publication, projectId, email);
        return new ResponseEntity<>(createdPublication, HttpStatus.CREATED);
    }


    @GetMapping("/publication/{projectId}")
    public ResponseEntity<List<Publication>> getPublicationsByProjectId(@PathVariable String projectId) {
        List<Publication> publications = publicationService.getPublicationsByProjectId(projectId);
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Publication> likePublication(@PathVariable String id, Principal principal) {
        try {
            Publication updatedPublication = publicationService.likePublication(id, principal);
            return ResponseEntity.ok(updatedPublication);
        } catch (OpenApiResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/{id}/dislike")
    public ResponseEntity<Publication> dislikePublication(@PathVariable String id, Principal principal) {
        try {
            Publication updatedPublication = publicationService.dislikePublication(id, principal);
            return ResponseEntity.ok(updatedPublication);
        } catch (OpenApiResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{publicationId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable String publicationId, @RequestBody Comment comment) {
        // Set the publication ID in the comment object
        Publication publication = new Publication();
        publication.setId(publicationId);
        comment.setPublication(publication);

        // Call the service method to add the comment
        Comment createdComment = publicationService.addComment(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{publicationId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPublicationId(@PathVariable String publicationId) {
        List<Comment> comments = publicationService.getCommentsByPublicationId(publicationId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
