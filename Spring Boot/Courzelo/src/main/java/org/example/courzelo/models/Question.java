package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "questions")
@Data
public class Question {
    @Id
    private String id;
    private String text;
    private List<String> options;
    private String QuizID;
    private String correctAnswer;
    private QuestionType type;
    private List<Answer> answers;
}
