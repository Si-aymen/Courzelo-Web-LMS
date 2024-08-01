package org.example.courzelo.controllers;


import lombok.AllArgsConstructor;
import org.example.courzelo.models.Stages;
import org.example.courzelo.models.Transports;
import org.example.courzelo.services.IStagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
@AllArgsConstructor
@PreAuthorize("permitAll()")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")

public class StageController {
    IStagesService stagesService ;

    @GetMapping("/GetAll")
    public List<Stages> getAllStages() {
        return stagesService.retrieveAllStages();
    }

    @GetMapping("/count/stages")
    public Long getNumberOfStage() {
        return stagesService.GetNumberOfStage();
    }

    @GetMapping("/GetById/stages/{stage-id}")
    public Stages getBlocs(@PathVariable("stage-id") String stagesId) {
        return stagesService.retrieveStage(stagesId);
    }


    @PostMapping("/add-stage")
    public Stages addStage(@RequestBody Stages stage) {
        return stagesService.addStage(stage);
    }

    @DeleteMapping("/remove-Stage/{stage-id}")
    public void removeTransports(@PathVariable("stage-id") String stageId) {
        stagesService.removeStage(stageId);
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignStudentToInternship(@RequestParam String studentId, @RequestParam String internshipId) {
        stagesService.AssignStudentToInternship(studentId, internshipId);
        return ResponseEntity.ok().build();
    }

}
