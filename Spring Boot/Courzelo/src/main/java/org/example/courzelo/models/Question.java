package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "questions")
@Data
public class Question {
    private String text;
    private List<String> options;
    private String correctAnswer;
    private QuestionType type;
}
