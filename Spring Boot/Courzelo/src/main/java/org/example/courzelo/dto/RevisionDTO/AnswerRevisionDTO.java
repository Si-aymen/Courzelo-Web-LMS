package org.example.courzelo.dto.RevisionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.example.courzelo.models.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRevisionDTO {

    private String id;

    private QuestionRevisionsDto question;

    private String answerText;

    private Boolean isCorrect;

    private User user;
}

