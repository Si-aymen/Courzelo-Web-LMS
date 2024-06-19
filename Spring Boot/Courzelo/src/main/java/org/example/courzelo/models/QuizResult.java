package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "grades")
@Data
public class QuizResult {
    @Id
    private String id;
    private String studentId;
    private double timeSpent;
    private double score;
}
