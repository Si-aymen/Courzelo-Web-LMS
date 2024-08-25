package org.example.courzelo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.Answer;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuizSubmissionDTO {
    private String quizID;
    private List<Answer> answers;
}
