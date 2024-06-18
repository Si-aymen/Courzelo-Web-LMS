package org.example.courzelo.services;

import org.example.courzelo.dto.AnswerDTO;
import org.example.courzelo.dto.EvaluationDTO;
import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.models.Answer;
import org.example.courzelo.models.Evaluation;
import org.example.courzelo.models.Question;
import org.example.courzelo.repositories.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    public EvaluationDTO createEvaluation(EvaluationDTO evaluationDTO) {
        Evaluation evaluation = new Evaluation();
        evaluation.setTitle(evaluationDTO.getTitle());
        evaluation.setDescription(evaluationDTO.getDescription());
        evaluation.setDate(evaluationDTO.getDate());
        evaluation.setTeacherId(evaluationDTO.getTeacherId());
        evaluation.setQuestions(evaluationDTO.getQuestions().stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList()));
        evaluation = evaluationRepository.save(evaluation);
        return mapToDTO(evaluation);
    }

    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EvaluationDTO getEvaluationById(String id) {
        return evaluationRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public void deleteEvaluation(String id) {
        evaluationRepository.deleteById(id);
    }

    private EvaluationDTO mapToDTO(Evaluation evaluation) {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setTitle(evaluation.getTitle());
        evaluationDTO.setDescription(evaluation.getDescription());
        evaluationDTO.setDate(evaluation.getDate());
        evaluationDTO.setTeacherId(evaluation.getTeacherId());
        evaluationDTO.setQuestions(evaluation.getQuestions().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));
        return evaluationDTO;
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

    private Question mapToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setQuestionText(questionDTO.getQuestionText());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setAnswers(questionDTO.getAnswers().stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList()));
        return question;
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
