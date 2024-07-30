package org.example.courzelo.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.models.Attendance;
import org.example.courzelo.models.AttendanceStatus;
import org.example.courzelo.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    public AttendanceDTO markAttendance(String studentId, AttendanceStatus status) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setDate(LocalDate.now());
        attendance.setStatus(status);
        attendance = attendanceRepository.save(attendance);

        return mapToDTO(attendance);
    }
    public List<AttendanceDTO> getAttendanceByStudentIdAndDate(String studentId, LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndDate(studentId, date);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByDate(date);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    private AttendanceDTO mapToDTO(Attendance attendance) {
        //Student student = studentRepository.findById(attendance.getStudentId()).orElse(null);
        AttendanceDTO dto = new AttendanceDTO();
        dto.setStudentId(attendance.getStudentId());
        //dto.setStudentName(student != null ? student.getName() : "Unknown");
        dto.setDate(attendance.getDate());
        dto.setStatus(attendance.getStatus());
        return dto;
    }
    private Attendance mapToEntity(AttendanceDTO dto) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(dto.getStudentId());
        attendance.setDate(dto.getDate());
        attendance.setStatus(dto.getStatus());
        return attendance;
    }
}
