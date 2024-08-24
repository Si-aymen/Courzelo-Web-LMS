package org.example.courzelo.controllers;

import org.example.courzelo.dto.ChromosomeDTO;
import org.example.courzelo.dto.ProfessorDTO;
import org.example.courzelo.dto.TimetableDTO;
import org.example.courzelo.services.TimetableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/timetable")
@CrossOrigin(origins = "http://localhost:4200")
public class TimetableController {
    @Autowired
    private TimetableService timetableService;

    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);

    @GetMapping("/generate")
    public ResponseEntity<TimetableDTO> generateTimetable(
            @RequestParam String courseIds,
            @RequestParam String professorIds) {

        try {
            List<String> courseIdList = Arrays.asList(courseIds.split(","));
            List<String> professorIdList = Arrays.asList(professorIds.split(","));

            ChromosomeDTO chromosome = timetableService.generateTimetable(courseIdList, professorIdList);
            TimetableDTO timetable = timetableService.convertToTimetable(chromosome);

            if (timetable != null) {
                return ResponseEntity.ok(timetable);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while generating timetable: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
   /* @GetMapping
    public ResponseEntity<List<TimetableDTO>> getTimetables() {
        List<TimetableDTO> timetable = timetableService.getTimetables();
        return ResponseEntity.ok(timetable);
    }*/
    @PostMapping( "/professor")
    public ResponseEntity<ProfessorDTO>AddProfessor(@RequestBody ProfessorDTO professorDTO){
        ProfessorDTO professor = timetableService.addProfessor(professorDTO);
        return ResponseEntity.ok(professor);
    }


}

