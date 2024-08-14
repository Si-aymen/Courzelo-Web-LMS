package org.example.courzelo.controllers;

import org.example.courzelo.dto.ProfessorDTO;
import org.example.courzelo.models.Professor;
import org.example.courzelo.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professors")
public class professorController {
    @Autowired
    private ProfessorService professorService;

    // Get professor by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable String id) {
        Professor professor = professorService.getProfessorById(id);
        ProfessorDTO professorDTO = new ProfessorDTO(professor.getProfessorId(), professor.getName(), professor.getUnavailableTimeSlots());
        return ResponseEntity.ok(professorDTO);
    }

    // Update unavailable time slots for a professor
    @PutMapping("/{id}/unavailable-timeslots")
    public ResponseEntity<ProfessorDTO> updateUnavailableTimeSlots(@PathVariable String id, @RequestBody  Professor unavailableTimeSlotsDTO) {
        Professor updatedProfessor = professorService.updateUnavailableTimeSlots(id, unavailableTimeSlotsDTO.getUnavailableTimeSlots());
        ProfessorDTO updatedProfessorDTO = new ProfessorDTO(updatedProfessor.getProfessorId(), updatedProfessor.getName(), updatedProfessor.getUnavailableTimeSlots());
        return ResponseEntity.ok(updatedProfessorDTO);
    }
}
