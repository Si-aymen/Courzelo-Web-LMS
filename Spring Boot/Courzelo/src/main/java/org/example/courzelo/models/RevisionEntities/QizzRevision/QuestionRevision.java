package org.example.courzelo.models.RevisionEntities.QizzRevision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "QuestionRevision")

public class QuestionRevision {
    @Id
    private String id;

    @Indexed
    private String text;

    @Indexed
    private String correctAnswer;

    // This field will store the user's answer
    @Indexed
    private String userAnswerText;

    @Indexed
    private Boolean isCorrect;

    @DBRef
    private List<AnswerRevision> answers;

    @JsonIgnore
    @DBRef
    private QuizRevision quizRevision;
}
