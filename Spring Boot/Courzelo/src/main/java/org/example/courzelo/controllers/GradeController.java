package org.example.courzelo.controllers;

import org.example.courzelo.dto.GradeDTO;
import org.example.courzelo.services.GradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public GradeDTO submitGrade(@RequestBody GradeDTO gradeDTO) {
        return gradeService.submitGrade(gradeDTO);
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<GradeDTO> getGradesByEvaluation(@PathVariable String evaluationId) {
        return gradeService.getGradesByEvaluation(evaluationId);
    }
}
