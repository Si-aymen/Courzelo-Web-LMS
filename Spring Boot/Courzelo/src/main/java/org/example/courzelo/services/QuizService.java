package org.example.courzelo.services;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.exceptions.ResourceNotFoundException;
import org.example.courzelo.models.*;
import org.example.courzelo.repositories.AnswerRepository;
import org.example.courzelo.repositories.QuestionRepository;
import org.example.courzelo.repositories.QuizRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);


    @Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionService = questionService;
    }

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

   /* public QuizDTO createQuiz(QuizDTO quizDTO) {
        Quiz quiz = mapToEntity(quizDTO, new Quiz());
        quiz = quizRepository.save(quiz);
        return mapToDTO(quiz);
    }*/
   @Transactional
   public Quiz updateQuiz1(String id, Quiz updatedQuiz) {
       if (quizRepository.existsById(id)) {
           updatedQuiz.setId(id);
           return quizRepository.save(updatedQuiz);
       } else {
           throw new ResourceNotFoundException("Quiz not found with id " + id);
       }
   }

    public QuizDTO updateQuiz(String id, Quiz quizDTO) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        updateQuizEntity(existingQuiz, quizDTO);

        Quiz savedQuiz = quizRepository.save(existingQuiz);
        return mapToDTO(savedQuiz);
    }

    private void updateQuizEntity(Quiz existingQuiz, Quiz quizDTO) {
        if (quizDTO.getTitle() != null) {
            existingQuiz.setTitle(quizDTO.getTitle());
        }
        if (quizDTO.getDescription() != null) {
            existingQuiz.setDescription(quizDTO.getDescription());
        }
        if (quizDTO.getQuestions() != null) {
            existingQuiz.setQuestions(quizDTO.getQuestions());
        }
    if (quizDTO.getDuration() != 0) {
        existingQuiz.setDuration(quizDTO.getDuration());

    }
    if (quizDTO.getMaxAttempts() != 0) {
        existingQuiz.setMaxAttempts(quizDTO.getMaxAttempts());
    }
    if (quizDTO.getScore() != 0) {
        existingQuiz.setScore(quizDTO.getScore());
    }
    if (quizDTO.getStatus() != null) {
        existingQuiz.setStatus(quizDTO.getStatus());
    }
    if (quizDTO.getCategory() != null) {
        existingQuiz.setCategory(quizDTO.getCategory());
    }
    }

    public QuizDTO updateQuizState(String QuizID ,Quiz updatedQuiz) {
        Quiz existingQuiz = quizRepository.findById(updatedQuiz.getId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + updatedQuiz.getId()));
        existingQuiz.setTitle(updatedQuiz.getTitle());
        existingQuiz.setDescription(updatedQuiz.getDescription());
        existingQuiz.setQuestions(updatedQuiz.getQuestions());
        Quiz savedQuiz = quizRepository.save(existingQuiz);
        return mapToDTO(savedQuiz);
    }

    public void deleteQuiz(String id) {
        quizRepository.deleteById(id);
    }

    public QuizDTO getQuizById(String id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        return mapToDTO(quiz);
    }
    public int getQuizDuration(String quizId) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        return quizOptional.map(Quiz::getDuration).orElse(0);
    }

    public QuizSubmissionResult submitQuiz(QuizSubmission submission) {
        Quiz quiz = quizRepository.findById(submission.getQuizID()).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Answer> answers = submission.getAnswers();
        int correctCount = 0;
        for (Answer answer : answers) {
            Optional<Question> question = questionRepository.findById(answer.getQuestionID());

            if (question.isPresent() && question.get().getCorrectAnswer().equals(answer.getAnswer())) {
                correctCount++;
            }
        }
        int score = (correctCount * 100) / quiz.getQuestions().size();

        QuizSubmissionResult result = new QuizSubmissionResult();
        result.setQuizID(submission.getQuizID());
        result.setScore(score);
        return result;
    }

    public QuizDTO createQuizWithQuestions(QuizDTO quizDTO,String email) {
        Quiz quiz = mapToEntity(quizDTO, new Quiz());
        quiz.setStatus(quizDTO.getStatus()); // explicitly set the status
        quiz.setUser(email);
        quiz = quizRepository.save(quiz);
        logger.info("Quiz ID after save: {}, Status: {}", quiz.getId(), quiz.getStatus());
        final String quizId = quiz.getId();
        List<Question> questions = quizDTO.getQuestions().stream()
                .map(questionDTO -> {
                    Question question = mapToQuestionEntity(questionDTO);
                    question.setQuizID(quizId);
                    question = questionRepository.save(question);
                    logger.info("Question ID after save: {}", question.getId());
                    return question;
                })
                .collect(Collectors.toList());
        quiz.setQuestions(questions);
        quiz = quizRepository.save(quiz);
        logger.info("Final Quiz ID: {}, Status: {}", quiz.getId(), quiz.getStatus());
        return mapToDTO(quiz);
    }

    public QuizDTO createQuizWithAnswers(QuizDTO quizDTO,String email) {
        Quiz quiz = mapToEntity(quizDTO, new Quiz());
        quiz.setUser(email);
        Quiz savedQuiz = quizRepository.save(quiz);
        for (Question question : quiz.getQuestions()) {
            question.setQuizID(savedQuiz.getId());
            questionRepository.save(question);
            for (Answer answer : question.getAnswers()) {
                answer.setQuestionID(question.getId());
                answerRepository.save(answer);
            }
        }
        return mapToDTO(savedQuiz);
    }

    public QuizDTO getQuizWithAnswersById(String id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        for (Question question : quiz.getQuestions()) {
            question.setAnswers(answerRepository.findByQuestionID(question.getId()));
        }
        return mapToDTO(quiz);
    }

    public QuizDTO mapToDTO(final Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        return mapToDTO(quiz, quizDTO);
    }

    private QuizDTO mapToDTO(final Quiz quiz, final QuizDTO quizDTO) {
        quizDTO.setUserEmail(quiz.getUser());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setQuestions(quiz.getQuestions().stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList()));
        quizDTO.setDuration(quiz.getDuration());
        quizDTO.setMaxAttempts(quiz.getMaxAttempts());
        quizDTO.setScore(quiz.getScore());
        quizDTO.setStatus(quiz.getStatus());
        quizDTO.setCategory(quiz.getCategory());
        quizDTO.setSelected(quiz.isSelected());
        return quizDTO;
    }
    public Quiz mapToEntity(final QuizDTO quizDTO, final Quiz quiz) {
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setQuestions(quizDTO.getQuestions().stream()
                .map(this::mapToQuestionEntity)
                .collect(Collectors.toList()));
        quiz.setDuration(quizDTO.getDuration());
        quiz.setMaxAttempts(quizDTO.getMaxAttempts());
        quiz.setScore(quizDTO.getScore());
        quiz.setStatus(quizDTO.getStatus());
        quiz.setCategory(quizDTO.getCategory());
        quiz.setSelected(quizDTO.isSelected());
        return quiz;
    }
    public QuizDTO getQuizStatus(String id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        return mapToDTO(quiz);
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
