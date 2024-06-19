package org.example.courzelo.services;

import org.example.courzelo.dto.AssignmentDTO;
import org.example.courzelo.models.Assignment;
import org.example.courzelo.repositories.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignementService {
    private final AssignmentRepository assignmentRepository;

    public AssignementService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public List<AssignmentDTO> findByStudentId(String studentId) {
        return assignmentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssignmentDTO saveAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = convertToEntity(assignmentDTO);
        assignment = assignmentRepository.save(assignment);
        return convertToDTO(assignment);
    }

    public double calculateAssignmentCompletionRate(String studentId) {
        List<Assignment> assignments = assignmentRepository.findByStudentId(studentId);
        long completedCount = assignments.stream().filter(Assignment::isCompleted).count();
        return (double) completedCount / assignments.size();
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setStudentId(assignment.getStudentId());
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setCompleted(assignment.isCompleted());
        dto.setTotalMarks(assignment.getTotalMarks());
        dto.setMarksObtained(assignment.getMarksObtained());
        return dto;
    }

    private Assignment convertToEntity(AssignmentDTO dto) {
        Assignment assignment = new Assignment();
        assignment.setId(dto.getId());
        assignment.setStudentId(dto.getStudentId());
        assignment.setAssignmentId(dto.getAssignmentId());
        assignment.setCompleted(dto.isCompleted());
        assignment.setTotalMarks(dto.getTotalMarks());
        assignment.setMarksObtained(dto.getMarksObtained());
        return assignment;
    }
}
