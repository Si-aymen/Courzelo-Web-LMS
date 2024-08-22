package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuizResultDTO {
    private String id;
    private String studentId;
    private double timeSpent;
    private double score;
}
