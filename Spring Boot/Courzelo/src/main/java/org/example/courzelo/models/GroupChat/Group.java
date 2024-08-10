package org.example.courzelo.models.GroupChat;

import lombok.Data;
import org.example.courzelo.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "chatgroups")
public class Group {
    @Id
    private String id;
    private String name;
    @DBRef
    private User creator;
    private List<String> members; // List of user IDs
}
