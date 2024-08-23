package org.example.courzelo.dto.RevisionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.RevisionEntities.QizzRevision.AnswerRevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRevisionsDto {

    private String id;

    private String text;

    private String correctAnswer;

    private String userAnswerText;

    private Boolean isCorrect;

    private List<AnswerRevisionDTO> answers;

    private QuizRevisionDTO quizRevision;
}


