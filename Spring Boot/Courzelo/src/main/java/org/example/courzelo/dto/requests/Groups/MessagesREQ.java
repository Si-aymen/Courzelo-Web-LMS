package org.example.courzelo.dto.requests.Groups;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import org.example.courzelo.models.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

public class MessagesREQ {
    private String id;
    private String groupId;
    private String senderId; // User ID of the sender
    private String text;

    private LocalDateTime dateCreation = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
