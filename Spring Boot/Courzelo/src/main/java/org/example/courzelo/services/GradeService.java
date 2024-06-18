package org.example.courzelo.services;

import org.example.courzelo.dto.GradeDTO;
import org.example.courzelo.models.Grade;
import org.example.courzelo.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {
    @Autowired
    private GradeRepository gradeRepository;

    public GradeDTO submitGrade(GradeDTO gradeDTO) {
        Grade grade = new Grade();
        grade.setId(gradeDTO.getId());
        grade.setScore(gradeDTO.getScore());
        grade.setFeedback(gradeDTO.getFeedback());
        grade.setStudentId(gradeDTO.getStudentId());
        grade.setEvaluationId(gradeDTO.getEvaluationId());

        grade = gradeRepository.save(grade);

        return mapToDTO(grade);
    }

    public List<GradeDTO> getGradesByEvaluation(String evaluationId) {
        return gradeRepository.findByEvaluationId(evaluationId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private GradeDTO mapToDTO(Grade grade) {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setId(grade.getId());
        gradeDTO.setScore(grade.getScore());
        gradeDTO.setFeedback(grade.getFeedback());
        gradeDTO.setStudentId(grade.getStudentId());
        gradeDTO.setEvaluationId(grade.getEvaluationId());
        return gradeDTO;
    }
}
