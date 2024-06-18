package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "answers")
@Data
public class Answer {
    @Id
    private String id;
    private String answerText;
    private boolean correct;// QCM questions
}
