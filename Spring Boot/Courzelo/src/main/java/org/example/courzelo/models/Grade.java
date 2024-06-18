package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "grades")
@Data
public class Grade {
    @Id
    private String id;
    private Double score;
    private String feedback;
    private String studentId;
    private String evaluationId;
}
