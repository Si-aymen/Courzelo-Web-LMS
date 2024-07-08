package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "quizSubmissionResults")
@Data
public class QuizSubmissionResult {
    private String quizID;
    private int score;
    private Status status;
    private String userID;
    private Date submittedAt;
    private List<Answer> answers;

}
