package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.QuestionType;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuestionDTO {
    private String id;
    private String text;
    private List<String> options;
    private String correctAnswer;
    private QuestionType type;
    private String QuizID;
}
