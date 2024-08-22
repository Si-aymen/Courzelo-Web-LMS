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

    public List<AssignmentDTO> findByStudentEmail(String studentEmail) {
        return assignmentRepository.findByStudent(studentEmail).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssignmentDTO saveAssignment(AssignmentDTO assignmentDTO, String studentEmail) {
        Assignment assignment = convertToEntity(assignmentDTO);
        assignment.setStudent(studentEmail);
        assignment = assignmentRepository.save(assignment);
        return convertToDTO(assignment);
    }

    public double calculateAssignmentCompletionRate(String studentEmail) {
        List<Assignment> assignments = assignmentRepository.findByStudent(studentEmail);
        long completedCount = assignments.stream().filter(Assignment::isCompleted).count();
        return (double) completedCount / assignments.size();
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setStudentEmail(assignment.getStudent());
        dto.setCompleted(assignment.isCompleted());
        dto.setTotalMarks(assignment.getTotalMarks());
        dto.setMarksObtained(assignment.getMarksObtained());
        return dto;
    }

    private Assignment convertToEntity(AssignmentDTO dto) {
        Assignment assignment = new Assignment();
        assignment.setId(dto.getId());
        assignment.setAssignmentId(dto.getAssignmentId());
        assignment.setCompleted(dto.isCompleted());
        assignment.setTotalMarks(dto.getTotalMarks());
        assignment.setMarksObtained(dto.                                        getMarksObtained());
        return assignment;
    }
}
