package org.example.courzelo.projecttest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.example.courzelo.models.ProjectEntities.project.Project;
import org.example.courzelo.models.ProjectEntities.publication.Comment;
import org.example.courzelo.models.ProjectEntities.publication.Publication;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.ProjectRepo.ProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.PublicationRepository;
import org.example.courzelo.repositories.ProjectRepo.commentRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Project.PublicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class PublicationServiceTest {

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepo projectRepository;

    @Mock
    private commentRepository commentRepository;

    @InjectMocks
    private PublicationServiceImpl publicationService;

    private User user;
    private Project project;
    private Publication publication;

    @Mock
    private Principal principal;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setId("userId");

        project = new Project();
        project.setId("projectId");

        publication = new Publication();
        publication.setContent("Sample content");
        publication.setId("publicationId");
        publication.setAuthor(user);
        publication.setProject(project);
        publication.setLikes(0);
        publication.setDislikes(0);
        publication.setCommentsCount(0);
        publication.setComments(Arrays.asList());
    }

    @Test
    void testCreatePublication() {
        // Mock behaviors
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
        when(projectRepository.findById("projectId")).thenReturn(Optional.of(project));
        when(publicationRepository.save(any(Publication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Create the publication
        Publication createdPublication = publicationService.createPublication(publication, "projectId", "test@example.com");

        // Assertions with output
        assertNotNull(createdPublication);
        System.out.println("Created Publication: " + createdPublication);

        assertEquals("Sample content", createdPublication.getContent());
        System.out.println("Publication Content: " + createdPublication.getContent());

        assertEquals(user, createdPublication.getAuthor());
        System.out.println("Publication Author: " + createdPublication.getAuthor());

        assertEquals(project, createdPublication.getProject());
        System.out.println("Publication Project: " + createdPublication.getProject());

        assertEquals(0, createdPublication.getLikes());
        System.out.println("Publication Likes: " + createdPublication.getLikes());

        assertEquals(0, createdPublication.getDislikes());
        System.out.println("Publication Dislikes: " + createdPublication.getDislikes());

        assertEquals(0, createdPublication.getCommentsCount());
        System.out.println("Publication Comments Count: " + createdPublication.getCommentsCount());

        assertTrue(createdPublication.getComments().isEmpty());
        System.out.println("Publication Comments: " + createdPublication.getComments());

        // Verifications
        verify(userRepository).findUserByEmail("test@example.com");
        verify(projectRepository).findById("projectId");
        verify(publicationRepository).save(createdPublication);
    }


    @Test
    void testAddComment() {
        // Create test data
        String publicationId = "publicationId";
        Publication publication = new Publication();
        publication.setId(publicationId);

        Comment comment = new Comment();
        comment.setPublication(publication); // Set the publication with ID

        Comment savedComment = new Comment();
        savedComment.setId("commentId"); // Simulate saved comment with an ID

        // Mock repository behavior
        when(publicationRepository.findById(publicationId)).thenReturn(Optional.of(publication));
        when(commentRepository.save(comment)).thenReturn(savedComment);

        // Call the method
        Comment result = publicationService.addComment(comment);

        // Verify the interactions
        verify(publicationRepository).findById(publicationId);
        verify(commentRepository).save(comment);

        // Verify the result
        assertEquals(savedComment, result);
        assertEquals(publication, result.getPublication());
    }

    @Test
    void testLikePublication_UserAuthenticated() {
        // Arrange
        String publicationId = "publicationId";
        String currentUserId = "userId";

        Publication publication = new Publication();
        publication.setId(publicationId);
        publication.setLikes(0);
        publication.setDislikes(1);
        publication.setUserReactions(new HashMap<>());

        // Mock the behavior of principal to simulate an authenticated user
        when(principal.getName()).thenReturn(currentUserId);

        // Mock the repository to return a valid publication
        when(publicationRepository.findById(publicationId)).thenReturn(Optional.of(publication));

        // Act
        Publication updatedPublication = publicationService.likePublication(publicationId, principal);

        // Assertions
        assertNotNull(updatedPublication);
        assertEquals(1, updatedPublication.getLikes(), "The like count should be incremented.");
        assertEquals("like", updatedPublication.getUserReactions().get(currentUserId), "User reaction should be 'like'.");

        // Verify interactions
        verify(publicationRepository).save(updatedPublication);
    }



}