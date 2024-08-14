package org.example.courzelo.controllers;

import org.example.courzelo.dto.ProfessorDTO;
import org.example.courzelo.models.Professor;
import org.example.courzelo.models.Timeslot;
import org.example.courzelo.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/professors")
public class professorController {
    @Autowired
    private ProfessorService professorService;
    @GetMapping
    public List<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }

    @GetMapping("/{professorId}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable String professorId) {
        Optional<Professor> professor = professorService.getProfessorById(professorId);
        return professor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Professor addProfessor(@RequestBody Professor professor) {
        return professorService.addProfessor(professor);
    }

    @PutMapping("/{professorId}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable String professorId, @RequestBody ProfessorDTO professorDTO) {
        Optional<Professor> existingProfessor = professorService.getProfessorById(professorId);
        if (existingProfessor.isPresent()) {
            Professor professor = existingProfessor.get();
            professor.setName(professorDTO.getName());
            professor.setUnavailableTimeSlots(professorDTO.getUnavailableTimeSlots());
            return ResponseEntity.ok(professorService.updateProfessor(professor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{professorId}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable String professorId) {
        professorService.deleteProfessor(professorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{professorId}/unavailable-timeslots")
    public ResponseEntity<Professor> updateUnavailableTimeSlots(@PathVariable String professorId, @RequestBody Set<Timeslot> unavailableTimeSlots) {
        Professor updatedProfessor = professorService.updateUnavailableTimeSlots(professorId, unavailableTimeSlots);
        if (updatedProfessor != null) {
            return ResponseEntity.ok(updatedProfessor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
