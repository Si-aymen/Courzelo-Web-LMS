package org.example.courzelo.models.ProjectEntities.ChatProject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.ProjectEntities.ChatProject.Chat;
import org.springframework.data.mongodb.core.mapping.Document;
import nonapi.io.github.classgraph.json.Id;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ChatCollection")
public class ChatCollection {
    @Id
    private String id;

    private List<Chat> chats;
}
