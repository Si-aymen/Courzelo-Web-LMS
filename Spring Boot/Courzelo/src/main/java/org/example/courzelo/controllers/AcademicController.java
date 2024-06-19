package org.example.courzelo.controllers;

import org.example.courzelo.dto.AcademicDTO;
import org.example.courzelo.services.AcademicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
public class AcademicController {
    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<AcademicDTO>> getAcademicByStudentId(@PathVariable String studentId) {
        List<AcademicDTO> academicList = academicService.findByStudentId(studentId);
        return ResponseEntity.ok(academicList);
    }

    @PostMapping
    public ResponseEntity<AcademicDTO> saveAcademic(@RequestBody AcademicDTO academicDTO) {
        AcademicDTO savedAcademic = academicService.saveAcademic(academicDTO);
        return new ResponseEntity<>(savedAcademic, HttpStatus.CREATED);
    }
}
