package org.example.courzelo.controllers;

import org.example.courzelo.dto.ParticipationDTO;
import org.example.courzelo.services.ParticipationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/participation")
public class PartcipationController {
    private final ParticipationService participationService;

    public PartcipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping("/{studentEmail}")
    public ResponseEntity<List<ParticipationDTO>> getParticipationByStudentEmail(@PathVariable String studentEmail) {
        List<ParticipationDTO> participationList = participationService.findByStudentEmail(studentEmail);
        return ResponseEntity.ok(participationList);
    }

    @PostMapping
    public ResponseEntity<ParticipationDTO> saveParticipation(@RequestBody ParticipationDTO participationDTO, Principal principal) {
        ParticipationDTO savedParticipation = participationService.saveParticipation(participationDTO,principal.getName());
        return new ResponseEntity<>(savedParticipation, HttpStatus.CREATED);
    }
}
