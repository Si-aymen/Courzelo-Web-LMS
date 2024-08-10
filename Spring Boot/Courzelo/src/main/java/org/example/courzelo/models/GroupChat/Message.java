package org.example.courzelo.models.GroupChat;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Data;
import org.example.courzelo.models.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "groupmessages")
public class Message {
    @Id
    private String id;
    @DBRef
    private Group group;
    @DBRef// Group ID to which the message belongs
    private User sender; // User ID of the sender
    private String text;
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate = LocalDateTime.now();
}