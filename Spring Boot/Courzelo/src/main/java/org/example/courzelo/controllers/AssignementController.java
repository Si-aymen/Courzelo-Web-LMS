package org.example.courzelo.controllers;

import org.example.courzelo.dto.AssignmentDTO;
import org.example.courzelo.services.AssignementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/assignements")
public class AssignementController {
    private final AssignementService assignmentService;
    public AssignementController(AssignementService assignmentService) {
        this.assignmentService = assignmentService;
    }
    @GetMapping("/{studentEmail}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByStudentEmail(@PathVariable String studentEmail) {
        List<AssignmentDTO> assignmentList = assignmentService.findByStudentEmail(studentEmail);
        return ResponseEntity.ok(assignmentList);
    }
    @PostMapping("/create")
    public ResponseEntity<AssignmentDTO> saveAssignment(@RequestBody AssignmentDTO assignmentDTO, Principal principal) {
        AssignmentDTO savedAssignment = assignmentService.saveAssignment(assignmentDTO,principal.getName());
        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
    }
}
