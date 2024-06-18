package org.example.courzelo.services;

import org.example.courzelo.dto.AnswerDTO;
import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.models.Answer;
import org.example.courzelo.models.Question;
import org.example.courzelo.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setAnswers(questionDTO.getAnswers().stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList()));
        question = questionRepository.save(question);
        return mapToDTO(question);
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(String id) {
        return questionRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }

    private QuestionDTO mapToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestionText(question.getQuestionText());
        questionDTO.setQuestionType(question.getQuestionType());
        questionDTO.setAnswers(question.getAnswers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));
        return questionDTO;
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
