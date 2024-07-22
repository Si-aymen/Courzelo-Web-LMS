package org.example.courzelo.services;

import org.example.courzelo.models.Evaluation;
import org.example.courzelo.repositories.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {
/*    private final EvaluationRepository evaluationRepository;
    private final QuizResultService quizResultService;
    private final AttendanceService attendanceService;
    private final AssignementService assignmentService;
    private final ParticipationService participationService;
    private final AcademicService academicService;

    public EvaluationService(EvaluationRepository evaluationRepository, QuizResultService quizResultService, AttendanceService attendanceService, AssignementService assignmentService, ParticipationService participationService, AcademicService academicService) {
        this.evaluationRepository = evaluationRepository;
        this.quizResultService = quizResultService;
        this.attendanceService = attendanceService;
        this.assignmentService = assignmentService;
        this.participationService = participationService;
        this.academicService = academicService;
    }

    public Evaluation evaluateStudent(String studentId) {
        double quizTimeSpent = quizResultService.getTotalQuizTimeSpent(studentId);
        double averageQuizScore = quizResultService.calculateAverageQuizScore(studentId);
        int attendanceCount = attendanceService.getAttendanceCount(studentId);
        double assignmentCompletionRate = assignmentService.calculateAssignmentCompletionRate(studentId);
        int participationScore = participationService.getParticipationScore(studentId);
        double academicScore = academicService.getAcademicScore(studentId);

        Evaluation evaluation = new Evaluation(studentId, quizTimeSpent,averageQuizScore, attendanceCount,
                assignmentCompletionRate, participationScore, academicScore);
        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getEvaluationByStudentId(String studentId) {
        return evaluationRepository.findByStudentId(studentId);
    }*/
}
