package org.example.courzelo.services;

import org.example.courzelo.dto.AnswerDTO;
import org.example.courzelo.models.Answer;
import org.example.courzelo.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setAnswerText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.isCorrect());
        answer = answerRepository.save(answer);
        return mapToDTO(answer);
    }

    public List<AnswerDTO> getAllAnswers() {
        return answerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AnswerDTO getAnswerById(String id) {
        return answerRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public void deleteAnswer(String id) {
        answerRepository.deleteById(id);
    }

    private AnswerDTO mapToDTO(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setAnswerText(answer.getAnswerText());
        answerDTO.setCorrect(answer.isCorrect());
        return answerDTO;
    }

    private Answer mapToEntity(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setId(answerDTO.getId());
        answer.setAnswerText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.isCorrect());
        return answer;
    }
}
