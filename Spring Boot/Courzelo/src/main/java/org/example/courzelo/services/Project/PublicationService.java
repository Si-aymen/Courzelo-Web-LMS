package org.example.courzelo.services.Project;

import org.example.courzelo.models.ProjectEntities.Comment;
import org.example.courzelo.models.ProjectEntities.Publication;
import org.example.courzelo.models.ProjectEntities.Reaction;

import java.security.Principal;
import java.util.List;

public interface PublicationService {
    Publication createPublication(Publication publication, String projectId, String email);
    List<Publication> getPublicationsByProjectId(String projectId);
    Publication likePublication(String publicationId, Principal principal) ;
    public Publication dislikePublication(String publicationId , Principal principal);
    String getCurrentUserId(Principal principal);
    Comment addComment(Comment comment) ;
    List<Comment> getCommentsByPublicationId(String publicationId) ;
}
