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
@CrossOrigin(origins = "http://localhost:4200")
public class professorController {
    @Autowired
    private ProfessorService professorService;
    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        List<ProfessorDTO> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }

    // Get a professor by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable("id") String professorId) {
        Optional<ProfessorDTO> professor = professorService.getProfessorById(professorId);
        return professor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new professor
    @PostMapping
    public ResponseEntity<ProfessorDTO> addProfessor(@RequestBody Professor professor) {
        ProfessorDTO createdProfessor = professorService.addProfessor(professor);
        return ResponseEntity.ok(createdProfessor);
    }

    // Update an existing professor
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> updateProfessor(@PathVariable("id") String professorId, @RequestBody Professor professor) {
        professor.setId(professorId);
        ProfessorDTO updatedProfessor = professorService.updateProfessor(professor);
        return ResponseEntity.ok(updatedProfessor);
    }

    // Delete a professor by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable("id") String professorId) {
        professorService.deleteProfessor(professorId);
        return ResponseEntity.noContent().build();
    }

    // Get unavailable time slots for a professor
    @GetMapping("/{id}/unavailable-timeslots")
    public ResponseEntity<Set<Timeslot>> getUnavailableTimeSlots(@PathVariable("id") String professorId) {
        Set<Timeslot> timeSlots = professorService.getUnavailableTimeSlots(professorId);
        return timeSlots != null ? ResponseEntity.ok(timeSlots) : ResponseEntity.notFound().build();
    }

    // Update unavailable time slots for a professor
    @PutMapping("/{id}/unavailable-timeslots")
    public ResponseEntity<ProfessorDTO> updateUnavailableTimeSlots(
            @PathVariable("id") String professorId, @RequestBody Set<Timeslot> unavailableTimeSlots) {
        ProfessorDTO updatedProfessor = professorService.updateUnavailableTimeSlots(professorId, unavailableTimeSlots);
        return updatedProfessor != null ? ResponseEntity.ok(updatedProfessor) : ResponseEntity.notFound().build();
    }
    @GetMapping("/names")
    public ResponseEntity<List<ProfessorDTO>> getAllProfessorNames() {
        List<ProfessorDTO> professors = ProfessorService.getAllProfessorNames();
        return ResponseEntity.ok(professors);
    }

}
