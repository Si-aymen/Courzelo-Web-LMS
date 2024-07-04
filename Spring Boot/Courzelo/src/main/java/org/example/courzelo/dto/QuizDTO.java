package org.example.courzelo.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.Question;
import org.springframework.data.annotation.Id;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuizDTO {
    private String id;
    private String title;
    private String description;
    private List<QuestionDTO> questions;
    private boolean isSelected;
    private double score;
    private String Status;
    private int duration; // in minutes
    private int maxAttempts;
    private String category;
}
