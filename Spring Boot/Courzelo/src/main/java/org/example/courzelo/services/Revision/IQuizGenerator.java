package org.example.courzelo.services.Revision;

import org.example.courzelo.models.RevisionEntities.QizzRevision.AnswerRevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuestionRevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IQuizGenerator {
    void generateQuestionsFromPdf(String fileId, String revisionId) throws IOException ;
    List<QuestionRevision> parseQuestionsFromText(String text);
    String getRevisionId(String fileId);
    String getFilePath(String fileId);
    List<QuizRevision> getQuizByRevisionId(String revisionId);
    List<QuestionRevision> getQuestionsByQuizRevisionId(String quizRevisionId);
    boolean checkAnswer(String questionId, String userAnswerText);
}
