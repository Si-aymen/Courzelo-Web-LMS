package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AssignmentDTO {
    private String id;
    private String studentId;
    private String assignmentId;
    private boolean completed;
    private double totalMarks;
    private double marksObtained;
}
