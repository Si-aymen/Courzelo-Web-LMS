package org.example.courzelo.services;

import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.dto.QuizResultDTO;
import org.example.courzelo.models.Attendance;
import org.example.courzelo.models.Question;
import org.example.courzelo.models.Quiz;
import org.example.courzelo.models.QuizResult;
import org.example.courzelo.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public QuizDTO getQuizById(String id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        return mapToDTO(quiz);
    }

    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Quiz quiz = mapToEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        return mapToDTO(quiz);
    }

    public String create(final QuizDTO quizDTO) {
        final Quiz quiz = new Quiz();
        mapToEntity(quizDTO, quiz);
        return quizRepository.save(quiz).getId();
    }

    public QuizDTO updateQuiz(String id, QuizDTO quizDTO) {
        Quiz quiz = mapToEntity(quizDTO);
        quiz.setId(id);
        quiz = quizRepository.save(quiz);
        return mapToDTO(quiz);
    }

    public void deleteQuiz(String id) {
        quizRepository.deleteById(id);
    }

    private QuizDTO mapToDTO(final Quiz quiz) {
        if (quiz == null) {
            return null;
        }

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setQuestions(quiz.getQuestions().stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList()));
        quizDTO.setDuration(quiz.getDuration());
        quizDTO.setMaxAttempts(quiz.getMaxAttempts());
        quizDTO.setScore(quiz.getScore());
        return quizDTO;
    }

    private Quiz mapToEntity(final QuizDTO quizDTO) {
        if (quizDTO == null) {
            return null;
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setQuestions(quizDTO.getQuestions().stream()
                .map(this::mapToQuestionEntity)
                .collect(Collectors.toList()));
        quiz.setDuration(quizDTO.getDuration());
        quiz.setMaxAttempts(quizDTO.getMaxAttempts());
        quiz.setScore(quizDTO.getScore());
        return quiz;
    }

    private Quiz mapToEntity(final QuizDTO quizDTO, final Quiz quiz) {
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setQuestions(quizDTO.getQuestions().stream()
                .map(this::mapToQuestionEntity)
                .collect(Collectors.toList()));
        quiz.setDuration(quizDTO.getDuration());
        quiz.setMaxAttempts(quizDTO.getMaxAttempts());
        quiz.setScore(quizDTO.getScore());
        return quiz;
    }

    private QuestionDTO mapToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setText(question.getText());
        questionDTO.setOptions(question.getOptions());
        questionDTO.setCorrectAnswer(question.getCorrectAnswer());
        questionDTO.setType(question.getType());
        return questionDTO;
    }

    private Question mapToQuestionEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setOptions(questionDTO.getOptions());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setType(questionDTO.getType());
        return question;
    }
}
