package org.example.courzelo.models.ProjectEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Publication")
public class Publication {

    @Id
    private String id;
    @Indexed
    private String content;
    @Indexed
    private LocalDateTime dateTime;
    @Indexed
    private int likes;
    @Indexed
    private int dislikes;
    @Indexed
    private Map<String, String> userReactions;
    @Indexed
    private int commentsCount;
    @DBRef
    private List<Comment> comments;
    @DBRef
    private User author;
    @DBRef
    private Project project;
}
