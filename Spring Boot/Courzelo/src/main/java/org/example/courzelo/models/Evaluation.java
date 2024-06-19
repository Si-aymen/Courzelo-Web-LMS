package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "evaluations")
@Data
public class Evaluation {
    @Id
    private String id;
    private String studentId;
    private double score;// Score obtained in the quiz
    private double quizTimeSpent;
    private int attendanceCount;
    private double assignmentCompletionRate;
    private int participationScore;
    private double academicScore;
    private Date date;

    public Evaluation(String studentId, double quizTimeSpent, double averageQuizScore, int attendanceCount, double assignmentCompletionRate, int participationScore, double academicScore) {
    }
}
