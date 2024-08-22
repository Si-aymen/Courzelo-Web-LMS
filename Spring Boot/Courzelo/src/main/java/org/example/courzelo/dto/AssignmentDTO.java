package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AssignmentDTO {
    private String id;
    private String assignmentId;
    private String studentEmail;
    private boolean completed;
    private double totalMarks;
    private double marksObtained;
}
