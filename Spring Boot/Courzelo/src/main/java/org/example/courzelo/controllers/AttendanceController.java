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
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<AttendanceDTO> markAttendance(@RequestParam String studentId,@RequestParam AttendanceStatus status) {
        AttendanceDTO attendance = attendanceService.markAttendance(studentId, status);
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

}
