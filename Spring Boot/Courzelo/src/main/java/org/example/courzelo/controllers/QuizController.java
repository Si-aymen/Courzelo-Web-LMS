package org.example.courzelo.controllers;

import jakarta.validation.Valid;
import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.models.Quiz;
import org.example.courzelo.models.QuizSubmission;
import org.example.courzelo.models.QuizSubmissionResult;
import org.example.courzelo.repositories.QuizRepository;
import org.example.courzelo.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    /*@PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) {
        QuizDTO createdQuiz = quizService.createQuiz(quizDTO);
        return ResponseEntity.ok(createdQuiz);
    }*/

    @PutMapping("/{quizId}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable String quizId, @RequestBody Quiz updatedQuiz) {
        try {
            QuizDTO updatedQuizDTO = quizService.updateQuiz(quizId, updatedQuiz);
            return ResponseEntity.ok(updatedQuizDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
   @PutMapping("/{id}")
   public ResponseEntity<QuizDTO> updateQuizState(@PathVariable String id, @RequestBody Quiz updatedQuiz) {
       try {
           QuizDTO updatedQuizDTO = quizService.updateQuiz(id, updatedQuiz);
           return ResponseEntity.ok(updatedQuizDTO);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }
    @DeleteMapping("/{id}")
    public ResponseEntity<QuizDTO> deleteQuiz(@PathVariable String id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/duration/{quizId}")
    @PreAuthorize("isAuthenticated()")
    public int getQuizDuration(@PathVariable String quizId) {
        return quizService.getQuizDuration(quizId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable String id) {
        QuizDTO quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuizSubmissionResult> submitQuiz(@RequestBody QuizSubmission submission) {
        QuizSubmissionResult result = quizService.submitQuiz(submission);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuizWithQuestions(@RequestBody QuizDTO quizDTO, Principal principal) {
        QuizDTO createdQuiz = quizService.createQuizWithQuestions(quizDTO,principal.getName());
        return ResponseEntity.ok(createdQuiz);
    }
    @GetMapping("/status/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuizDTO> getQuizStatus(@PathVariable String id) {
        QuizDTO quiz = quizService.getQuizStatus(id);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/with-answers")
    public ResponseEntity<QuizDTO> createQuizWithAnswers(@RequestBody QuizDTO quizDTO, Principal principal) {
        QuizDTO createdQuiz = quizService.createQuizWithAnswers(quizDTO,principal.getName());
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/with-answers/{id}")
    public ResponseEntity<QuizDTO> getQuizWithAnswersById(@PathVariable String id) {
        QuizDTO quiz = quizService.getQuizWithAnswersById(id);
        return ResponseEntity.ok(quiz);
    }
}
