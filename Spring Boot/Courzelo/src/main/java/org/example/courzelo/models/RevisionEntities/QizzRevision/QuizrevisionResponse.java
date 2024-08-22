package org.example.courzelo.models.RevisionEntities.QizzRevision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "QuizrevisionResponse")
public class QuizrevisionResponse {
    @Id
    private String id;

    @DBRef
    private QuizRevision quiz;

    @DBRef
    private User user;

    @DBRef
    private List<AnswerRevision> answers;


    private int score;
}
