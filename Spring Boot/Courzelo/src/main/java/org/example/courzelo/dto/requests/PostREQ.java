package org.example.courzelo.dto.requests;


import java.time.LocalDateTime;
import java.util.List;

public class PostREQ {

    private String postId;

    @Override
    public String toString() {
        return "PostREQ{" +
                "postId='" + postId + '\'' +
                ", postName='" + postName + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", user='" + user + '\'' +
                ", dateCreation=" + dateCreation +
                ", subforum='" + subforum + '\'' +
                '}';
    }

    private String postName;
    private String content;
    private String description;
    private String user;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private String subforum;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getSubforum() {
        return subforum;
    }

    public void setSubforum(String subforum) {
        this.subforum = subforum;
    }
}
