package org.example.courzelo.services;

import com.fasterxml.jackson.core.JsonParser;
import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.models.Question;
import org.example.courzelo.models.Quiz;
import org.example.courzelo.repositories.QuestionRepository;
import org.example.courzelo.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(String id) {
        Question question = questionRepository.findById(id).orElse(null);
        return convertToDTO(question);
    }

    public List<Question> getQuestionsByIds(List<String> ids) {
        return questionRepository.findByIdIn(ids);
    }


    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        question = questionRepository.save(question);
        return convertToDTO(question);
    }

    public QuestionDTO updateQuestion(String id, QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        question = questionRepository.save(question);
        return convertToDTO(question);
    }

    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }

    private QuestionDTO convertToDTO(Question question) {
        if (question == null) {
            return null;
        }
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setText(question.getText());
        questionDTO.setOptions(question.getOptions());
        questionDTO.setCorrectAnswer(question.getCorrectAnswer());
        questionDTO.setType(question.getType());
        return questionDTO;
    }

    private Question convertToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setOptions(questionDTO.getOptions());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setType(questionDTO.getType());
        return question;
    }
    
}
