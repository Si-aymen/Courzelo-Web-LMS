package org.example.courzelo.models.RevisionEntities.QizzRevision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection ="AnswerRevision")

public class AnswerRevision {
    @Id
    private String id;

    @DBRef
    private QuestionRevision question;

    @Indexed
    private String answerText;

    @Indexed
    private Boolean isCorrect;

    @DBRef
    private User user;
}

