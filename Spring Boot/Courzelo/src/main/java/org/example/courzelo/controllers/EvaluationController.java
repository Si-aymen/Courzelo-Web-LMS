package org.example.courzelo.controllers;

import org.example.courzelo.dto.EvaluationDTO;
import org.example.courzelo.services.EvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/evaluation")
    public EvaluationDTO createEvaluation(@RequestBody EvaluationDTO evaluationDTO) {
        return evaluationService.createEvaluation(evaluationDTO);
    }

    @GetMapping("/evaluation")
    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/evaluation/{id}")
    public EvaluationDTO getEvaluationById(@PathVariable String id) {
        return evaluationService.getEvaluationById(id);
    }

    @DeleteMapping("/evaluation/{id}")
    public void deleteEvaluation(@PathVariable String id) {
        evaluationService.deleteEvaluation(id);
    }
}
