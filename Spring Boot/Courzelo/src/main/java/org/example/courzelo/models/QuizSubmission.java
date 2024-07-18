package org.example.courzelo.models;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "quizSubmissions")
@Data
public class QuizSubmission {
    private String quizID;
    private List<Answer> answers;
}
