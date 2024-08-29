package org.example.courzelo.controllers.Revision;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;

import org.example.courzelo.models.RevisionEntities.QizzRevision.QuestionRevision;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.example.courzelo.services.Revision.IQuizGenerator;
import org.example.courzelo.services.Revision.PdfrevisionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "QuizzRevision")
public class QuizzRevisionController {

    private final PdfrevisionService pdfreService;
    @Autowired
    private IQuizGenerator quizService;

    @PostMapping("/generate-questions/{id}")
    public void generateQuestions(@PathVariable String id) {
        try {
            // Retrieve the file path and revision ID from the service
            String filePath = quizService.getFilePath(id);
            String revisionId = quizService.getRevisionId(id);

            // Generate questions from the PDF
            quizService.generateQuestionsFromPdf(filePath, revisionId);
        } catch (IOException e) {
            throw new RuntimeException("Error processing PDF", e);
        }
    }
    @GetMapping("/participaterevision/{revisionId}")
    public List<FileMetadatarevision> getFileByrevisionId(@PathVariable String revisionId) {
        return pdfreService.getFilesByrevisionId(revisionId);
    }

    @GetMapping("/participaterevision/{revisionId}/quizz")

    public ResponseEntity<List<QuizRevision>> getQuizByRevisionId(@PathVariable String revisionId) {
        List<QuizRevision> quizzes = quizService.getQuizByRevisionId(revisionId);
        return ResponseEntity.ok(quizzes);
    }
    @GetMapping("/quizrevision/{quizRevisionId}/questions")
    public ResponseEntity<List<QuestionRevision>> getQuestionsByQuizRevisionId(@PathVariable String quizRevisionId) {
        List<QuestionRevision> questions = quizService.getQuestionsByQuizRevisionId(quizRevisionId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submitAnswer")
    public ResponseEntity<Boolean> submitAnswer(@RequestParam("questionId") String questionId,
                                                @RequestParam("userAnswerText") String userAnswerText) {
        boolean isCorrect = quizService.checkAnswer(questionId, userAnswerText);
        return ResponseEntity.ok(isCorrect);
    }
}
