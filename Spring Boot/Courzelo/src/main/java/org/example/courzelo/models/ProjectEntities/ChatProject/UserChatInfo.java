package org.example.courzelo.models.ProjectEntities.ChatProject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.ProjectEntities.Project;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import nonapi.io.github.classgraph.json.Id;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UserChatInfo")
public class UserChatInfo {
    @Id
    private String id;
    @Indexed
    private String chatId;
    @Indexed
    private String contactId;
    @Indexed
    private String contactName;
    @Indexed
    private int unread;
    @Indexed
    private String lastChatTime; // Alternatively, use `Date lastChatTime;` and handle conversion

    @DBRef
    private Project project;

}
