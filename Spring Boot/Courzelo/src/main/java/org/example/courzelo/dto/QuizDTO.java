package org.example.courzelo.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.Question;
import org.example.courzelo.models.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuizDTO {
    private String id;
    private String userEmail;
    private String title;
    private String description;
    private List<QuestionDTO> questions;
    private String QuizID;
    private boolean isSelected;
    private double score;
    private Status status;
    private int duration; // in minutes
    private int maxAttempts;
    private String category;
    private String course;
}
