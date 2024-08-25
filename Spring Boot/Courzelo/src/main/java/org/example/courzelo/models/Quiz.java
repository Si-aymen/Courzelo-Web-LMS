package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.dto.QuestionDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "quizzes")
@Data
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
    private boolean isSelected;
    private double score;
    private Status status;
    private int duration; // in minutes
    private int maxAttempts;
    private String category;
    private String user;
    private String course;
}
