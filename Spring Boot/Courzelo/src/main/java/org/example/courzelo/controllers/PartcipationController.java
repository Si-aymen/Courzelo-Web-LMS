package org.example.courzelo.controllers;

import org.example.courzelo.dto.ParticipationDTO;
import org.example.courzelo.models.Participation;
import org.example.courzelo.services.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participation")
public class PartcipationController {
    private final ParticipationService participationService;

    public PartcipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<ParticipationDTO>> getParticipationByStudentId(@PathVariable String studentId) {
        List<ParticipationDTO> participationList = participationService.findByStudentId(studentId);
        return ResponseEntity.ok(participationList);
    }

    @PostMapping
    public ResponseEntity<ParticipationDTO> saveParticipation(@RequestBody ParticipationDTO participationDTO) {
        ParticipationDTO savedParticipation = participationService.saveParticipation(participationDTO);
        return new ResponseEntity<>(savedParticipation, HttpStatus.CREATED);
    }
}
