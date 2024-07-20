package org.example.courzelo.services;

import org.example.courzelo.dto.QuizResultDTO;
import org.example.courzelo.models.QuizResult;
import org.example.courzelo.repositories.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    public List<QuizResultDTO> findByStudentId(String studentEmail) {
        return quizResultRepository.findByStudent(studentEmail).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuizResultDTO saveQuizResult(QuizResultDTO quizResultDTO, String studentEmail) {
        QuizResult quizResult = convertToEntity(quizResultDTO);
        quizResult.setStudent(studentEmail);
        quizResult = quizResultRepository.save(quizResult);
        return convertToDTO(quizResult);
    }

    public double getTotalQuizTimeSpent(String studentEmail) {
        List<QuizResult> quizResults = quizResultRepository.findByStudent(studentEmail);
        return quizResults.stream().mapToDouble(QuizResult::getTimeSpent).sum();
    }

    public double calculateAverageQuizScore(String studentEmail) {
        List<QuizResult> quizResults = quizResultRepository.findByStudent(studentEmail);
        return quizResults.stream().mapToDouble(QuizResult::getScore).average().orElse(0.0);
    }

    private QuizResultDTO convertToDTO(QuizResult quizResult) {
        QuizResultDTO dto = new QuizResultDTO();
        dto.setId(quizResult.getId());
        dto.setStudentEmail(quizResult.getStudent());
        dto.setTimeSpent(quizResult.getTimeSpent());
        dto.setScore(quizResult.getScore());
        return dto;
    }

    private QuizResult convertToEntity(QuizResultDTO dto) {
        QuizResult quizResult = new QuizResult();
        quizResult.setId(dto.getId());
        quizResult.setTimeSpent(dto.getTimeSpent());
        quizResult.setScore(dto.getScore());
        return quizResult;
    }
}
