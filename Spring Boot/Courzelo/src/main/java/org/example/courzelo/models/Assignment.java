package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assignments")
@Data
public class Assignment {
    @Id
    private String id;
    private String studentId;
    private String assignmentId;
    private boolean completed;
    private double totalMarks;
    private double marksObtained;
}
