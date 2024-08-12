package org.example.courzelo.controllers;

import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.dto.AttendanceSettingsDTO;
import org.example.courzelo.models.AttendanceSettings;
import org.example.courzelo.models.AttendanceStatus;
import org.example.courzelo.services.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);



    @PostMapping("/mark")
    public ResponseEntity<AttendanceDTO> markAttendance(@RequestParam String studentId, @RequestParam AttendanceStatus status, @RequestParam(required = false, defaultValue = "0") int minutesLate) {
        AttendanceDTO attendance = attendanceService.markAttendance(studentId, status, minutesLate);
        return ResponseEntity.ok(attendance);
    }
    @GetMapping("/student/{studentId}/date/{date}")
    public ResponseEntity<List<AttendanceDTO>> getAttendance(@PathVariable String studentId, @PathVariable String date) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByStudentIdAndDate(studentId, LocalDate.parse(date));
         return ResponseEntity.ok(attendance);
    }
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(@PathVariable String date) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByDate(LocalDate.parse(date));
        return ResponseEntity.ok(attendance);
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable String studentId) {
        List<AttendanceDTO> attendanceRecords = attendanceService.getAttendanceByStudentId(studentId);
        return ResponseEntity.ok(attendanceRecords);
    }
    @GetMapping("/history")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceHistory(@RequestParam String studentId) {
        List<AttendanceDTO> attendanceHistory = attendanceService.getAttendanceHistory(studentId);
        return ResponseEntity.ok(attendanceHistory);
    }
    @GetMapping("/report")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceReport(
            @RequestParam String studentName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<AttendanceDTO> attendanceReport = attendanceService.getAttendanceReport(studentName, startDate, endDate);
        return ResponseEntity.ok(attendanceReport);
    }
    @GetMapping
    public ResponseEntity<AttendanceSettingsDTO> getSettings() {
        AttendanceSettingsDTO settings =attendanceService.getSettings();
        if (settings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<AttendanceSettingsDTO> saveSettings(@RequestBody AttendanceSettingsDTO settingsDTO) {
        try {
            logger.info("Saving settings: {}", settingsDTO);
            AttendanceSettingsDTO savedSettings = attendanceService.saveSettings(settingsDTO);
            logger.info("Settings saved successfully: {}", savedSettings);
            return new ResponseEntity<>(savedSettings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while saving settings: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setSettings(AttendanceSettingsDTO settings) {
        attendanceService.saveSettings(settings);
    }
}
