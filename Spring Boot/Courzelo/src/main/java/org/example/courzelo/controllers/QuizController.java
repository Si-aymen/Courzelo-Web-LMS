package org.example.courzelo.controllers;

import jakarta.validation.Valid;
import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.models.Quiz;
import org.example.courzelo.models.QuizSubmission;
import org.example.courzelo.models.QuizSubmissionResult;
import org.example.courzelo.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable String id) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        QuizDTO quizDTO = quizService.mapToDTO(quiz);
        return new ResponseEntity<>(quizDTO, HttpStatus.OK);
    }

   /* @PostMapping("/create")
    public ResponseEntity<String> createQuiz(
            @RequestBody @Valid final QuizDTO quizDTO) {
        final String createdId = quizService.create(quizDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }*/
   @PostMapping("/create")
   public ResponseEntity<String> createQuizWithQuestionsEndpoint(@RequestBody Quiz quiz) {
       Quiz createdQuiz = quizService.createQuizWithQuestions(quiz);
       String quizId = createdQuiz.getId();
       return new ResponseEntity<>(quizId, HttpStatus.CREATED);
   }
    @PostMapping


    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable String id, @RequestBody QuizDTO quizDTO) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
        if (updatedQuiz == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedQuiz, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/with-answers")
    public Quiz getQuizWithAnswersById(@PathVariable String id) {
        return quizService.getQuizWithAnswersById(id);
    }

    @PostMapping("/submit")
    public QuizSubmissionResult submitQuiz(@RequestBody QuizSubmission submission) {
        return quizService.submitQuiz(submission);
    }
}
