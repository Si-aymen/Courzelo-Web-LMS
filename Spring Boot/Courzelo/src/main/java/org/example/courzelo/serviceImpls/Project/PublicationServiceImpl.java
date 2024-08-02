package org.example.courzelo.serviceImpls.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.ProjectEntities.Comment;
import org.example.courzelo.models.ProjectEntities.Project;
import org.example.courzelo.models.ProjectEntities.Publication;
import org.example.courzelo.models.ProjectEntities.Reaction;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.ProjectRepo.ProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.PublicationRepository;
import org.example.courzelo.repositories.ProjectRepo.commentRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.Project.PublicationService;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;  // Use UserRepository instead of UserProfileRepository
    private final ProjectRepo projectRepository;
    private final commentRepository commentRepository;

    public Publication createPublication(Publication publication, String projectId, String email) {
        log.debug("Looking for user with name: {}", email);
        User author = userRepository.findUserByEmail(email)
               ;

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        publication.setAuthor(author);
        publication.setDateTime(LocalDateTime.now());
        publication.setLikes(0);
        publication.setDislikes(0);
        publication.setCommentsCount(0);
        publication.setComments(new ArrayList<>());
        publication.setProject(project);

        return publicationRepository.save(publication);
    }

    @Override
    public List<Publication> getPublicationsByProjectId(String projectId) {
        return publicationRepository.findByProjectId(projectId);
    }
    @Override
    public Publication likePublication(String publicationId, Principal principal) {
        Optional<Publication> optionalPublication = publicationRepository.findById(publicationId);
        if (optionalPublication.isPresent()) {
            Publication publication = optionalPublication.get();
            String currentUserId = getCurrentUserId(principal);

            if (currentUserId == null) {
                throw new IllegalArgumentException("User not authenticated");
            }

            if (publication.getUserReactions() == null) {
                publication.setUserReactions(new HashMap<>());
            }

            String reaction = publication.getUserReactions().get(currentUserId);
            if ("dislike".equals(reaction)) {
                publication.setDislikes(publication.getDislikes() - 1);
            }

            if (!"like".equals(reaction)) {
                publication.setLikes(publication.getLikes() + 1);
                publication.getUserReactions().put(currentUserId, "like");
                publicationRepository.save(publication);
            }

            return publication;
        } else {
            throw new OpenApiResourceNotFoundException("Publication not found");
        }
    }

    @Override
    public Publication dislikePublication(String publicationId, Principal principal) {
        Optional<Publication> optionalPublication = publicationRepository.findById(publicationId);
        if (optionalPublication.isPresent()) {
            Publication publication = optionalPublication.get();
            String currentUserId = getCurrentUserId(principal);

            if (currentUserId == null) {
                throw new IllegalArgumentException("User not authenticated");
            }

            if (publication.getUserReactions() == null) {
                publication.setUserReactions(new HashMap<>());
            }

            String reaction = publication.getUserReactions().get(currentUserId);
            if ("like".equals(reaction)) {
                publication.setLikes(publication.getLikes() - 1);
            }

            if (!"dislike".equals(reaction)) {
                publication.setDislikes(publication.getDislikes() + 1);
                publication.getUserReactions().put(currentUserId, "dislike");
                publicationRepository.save(publication);
            }

            return publication;
        } else {
            throw new OpenApiResourceNotFoundException("Publication not found");
        }
    }

    @Override
    public String getCurrentUserId(Principal principal) {
        if (principal != null) {
            String email = principal.getName(); // Assuming the principal name is the email
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                return user.getId();
            }
        }
        return null;
    }
    @Override
    public Comment addComment(Comment comment) {
        // Fetch the publication by ID from the comment's publication field
        String publicationId = comment.getPublication().getId();
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publication not found with id " + publicationId));

        // Set the fetched publication in the comment
        comment.setPublication(publication);

        // Save and return the comment
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPublicationId(String publicationId) {
        // Assuming you have a repository to fetch comments
        return commentRepository.findByPublicationId(publicationId);
    }



}

