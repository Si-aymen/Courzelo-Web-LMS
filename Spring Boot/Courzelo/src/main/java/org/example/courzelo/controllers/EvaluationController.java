package org.example.courzelo.controllers;

import org.example.courzelo.dto.EvaluationDTO;
import org.example.courzelo.models.Evaluation;
import org.example.courzelo.services.EvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {
  /*  private final EvaluationService evaluationService;
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }
    @GetMapping("/student/{studentId}")
    public List<Evaluation> getEvaluationByStudentId(@PathVariable String studentId) {
        return evaluationService.getEvaluationByStudentId(studentId);
    }
    @PostMapping("/evaluate/{studentId}")
    public Evaluation evaluateStudent(@PathVariable String studentId) {
        return evaluationService.evaluateStudent(studentId);
    }*/
}
