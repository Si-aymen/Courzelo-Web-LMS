package org.example.courzelo.controllers;

import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.models.Attendance;
import org.example.courzelo.services.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController( AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable String studentId) {
        List<AttendanceDTO> attendanceDTOs = attendanceService.getAttendanceByStudentId(studentId);
        return ResponseEntity.ok(attendanceDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<AttendanceDTO> saveAttendance(@RequestBody AttendanceDTO attendanceDTO, Principal principal) {
        AttendanceDTO savedAttendanceDTO = attendanceService.saveAttendance(attendanceDTO, principal.getName());
        return new ResponseEntity<>(savedAttendanceDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable String id, @RequestBody AttendanceDTO attendanceDTO) {
        attendanceDTO.setId(id);
        AttendanceDTO updatedAttendanceDTO = attendanceService.updateAttendance(attendanceDTO);
        return ResponseEntity.ok(updatedAttendanceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable String id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/student/{studentEmail}")
    public ResponseEntity<Integer> getAttendanceCount(@PathVariable String studentEmail) {
        int count = attendanceService.getAttendanceCount(studentEmail);
        return ResponseEntity.ok(count);
    }
}
