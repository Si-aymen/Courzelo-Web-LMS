package org.example.courzelo.services;

import org.example.courzelo.dto.QuestionDTO;
import org.example.courzelo.dto.QuizDTO;
import org.example.courzelo.models.*;
import org.example.courzelo.repositories.AnswerRepository;
import org.example.courzelo.repositories.QuestionRepository;
import org.example.courzelo.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;


    @Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
    public Quiz getQuizById(String id) {
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public QuizSubmissionResult submitQuiz(QuizSubmission submission) {
        Quiz quiz = getQuizById(submission.getQuizID());
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
    public Quiz createQuizWithQuestions(Quiz quiz) {
        quiz = quizRepository.save(quiz);

        List<Question> questions = quiz.getQuestions().stream()
                .map(questionDTO -> {
                    Question question = new Question();
                    question.setText(questionDTO.getText());
                    question.setOptions(questionDTO.getOptions());
                    question.setCorrectAnswer(questionDTO.getCorrectAnswer());
                    question.setType(questionDTO.getType());
                    question = questionRepository.save(question);
                    return question;
                })
                .collect(Collectors.toList());
        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }
    public Quiz CreateQuizWithAnswers(Quiz quiz) {
        Quiz savedQuiz = quizRepository.save(quiz);
        for (Question question : quiz.getQuestions()) {
            question.setQuizID(savedQuiz.getId());
            questionRepository.save(question);
            for (Answer answer : question.getAnswers()) {
                answer.setQuestionID(question.getId());
                answerRepository.save(answer);
            }
        }
        return savedQuiz;
    }
    public Quiz getQuizWithAnswersById(String id) {
        Quiz quiz = getQuizById(id);
        for (Question question : quiz.getQuestions()) {
            question.setAnswers(answerRepository.findByQuestionID(question.getId()));
        }
        return quiz;
    }

    public QuizDTO mapToDTO(final Quiz quiz) {
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
        quizDTO.setStatus(quiz.getStatus());
        quizDTO.setCategory(quiz.getCategory());
        quizDTO.setSelected(quiz.isSelected());
        return quizDTO;
    }

    private Quiz mapToEntity(final QuizDTO quizDTO) {
        if (quizDTO == null) {
            return null;
        }

        Quiz quiz = new Quiz();
        quiz.setId(quizDTO.getId());
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

    private Quiz mapToEntity(final QuizDTO quizDTO, final Quiz quiz) {
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
