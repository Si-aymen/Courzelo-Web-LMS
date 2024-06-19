package org.example.courzelo.controllers;

import org.example.courzelo.dto.AssignmentDTO;
import org.example.courzelo.models.Assignment;
import org.example.courzelo.services.AssignementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignements")
public class AssignementController {

    private final AssignementService assignmentService;

    public AssignementController(AssignementService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByStudentId(@PathVariable String studentId) {
        List<AssignmentDTO> assignmentList = assignmentService.findByStudentId(studentId);
        return ResponseEntity.ok(assignmentList);
    }

    @PostMapping
    public ResponseEntity<AssignmentDTO> saveAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        AssignmentDTO savedAssignment = assignmentService.saveAssignment(assignmentDTO);
        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
    }
}
