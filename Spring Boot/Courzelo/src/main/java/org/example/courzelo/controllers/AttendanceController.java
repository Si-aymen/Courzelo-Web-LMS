package org.example.courzelo.controllers;

import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.models.AttendanceStatus;
import org.example.courzelo.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/history")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceHistory(@RequestParam String studentId) {
        List<AttendanceDTO> attendanceHistory = attendanceService.getAttendanceHistory(studentId);
        return ResponseEntity.ok(attendanceHistory);
    }

}
