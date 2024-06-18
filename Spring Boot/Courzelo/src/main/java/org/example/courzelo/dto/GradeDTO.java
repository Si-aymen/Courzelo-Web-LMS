package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class GradeDTO {
    private String id;
    private Double score;
    private String feedback;
    private String studentId;
    private String evaluationId;
}
