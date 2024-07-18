package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class EvaluationDTO {
    private String id;
    private String studentId;
    private double score;// Score obtained in the quiz
    private double quizTimeSpent;
    private int attendanceCount;
    private double assignmentCompletionRate;
    private int participationScore;
    private double academicScore;
    private Date date;


}
