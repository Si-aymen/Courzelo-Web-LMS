package org.example.courzelo.controllers;

import org.example.courzelo.dto.AnswerDTO;
import org.example.courzelo.services.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/answer")
    public ResponseEntity<AnswerDTO> createAnswer(@RequestBody AnswerDTO answerDTO) {
        return ResponseEntity.ok(answerService.createAnswer(answerDTO));
    }

    @GetMapping("/answer")
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/answer/{id}")
    public ResponseEntity<AnswerDTO> getAnswerById(@PathVariable String id) {
        AnswerDTO answerDTO = answerService.getAnswerById(id);
        if (answerDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerDTO);
    }

    @DeleteMapping("/answer/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable String id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
