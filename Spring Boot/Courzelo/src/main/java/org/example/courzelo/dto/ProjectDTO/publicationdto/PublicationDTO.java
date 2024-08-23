package org.example.courzelo.dto.ProjectDTO.publicationdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.dto.ProjectDTO.projectdto.ProjectDTO;
import org.example.courzelo.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicationDTO {
    private String id;
    private String content;
    private LocalDateTime dateTime;
    private int likes;
    private int dislikes;
    private Map<String, String> userReactions;
    private int commentsCount;
    private List<CommentDTO> comments; // Assuming you have a CommentDTO
    private User author; // Mapping User entity to AuthorDTO
    private ProjectDTO project; // Mapping Project entity to ProjectDTO
}

