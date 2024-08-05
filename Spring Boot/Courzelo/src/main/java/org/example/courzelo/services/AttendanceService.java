package org.example.courzelo.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.dto.AttendanceSettingsDTO;
import org.example.courzelo.models.Attendance;
import org.example.courzelo.models.AttendanceSettings;
import org.example.courzelo.models.AttendanceStatus;
import org.example.courzelo.repositories.AttendanceRepository;
import org.example.courzelo.repositories.AttendanceSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    private AttendanceSettingsRepository repository;
    private static final Map<String, String> studentData = new HashMap<>();

    static {
        studentData.put("student1", "John Doe");
        studentData.put("student2", "Jane Smith");
        studentData.put("student3", "Alice Johnson");

    }
    public AttendanceDTO markAttendance(String studentId, AttendanceStatus status, int minutesLate) {
        String studentName = studentData.getOrDefault(studentId, "Unknown");
        if ("LATE".equals(status) && minutesLate > 15) {
            throw new RuntimeException("Attendance cannot be marked. The student is more than 15 minutes late.");
        }
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setDate(LocalDate.now());
        attendance.setStudentName(studentName);
        attendance.setStatus(status);
        attendance.setMinutesLate(minutesLate);
        attendance = attendanceRepository.save(attendance);

        return mapToDTO(attendance);
    }
    public List<AttendanceDTO> getAttendanceByStudentIdAndDate(String studentId, LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndDate(studentId, date);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<AttendanceDTO> getAttendanceHistory(String studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<AttendanceDTO> getAttendanceReport(String studentName, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByStudentNameAndDateBetween(studentName, startDate, endDate);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<AttendanceDTO> getAttendanceByStudentId(String studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByDate(date);
        return attendances.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public AttendanceSettingsDTO getSettings() {
        AttendanceSettings settings = repository.findById("1").orElse(new AttendanceSettings());
        return mapToDTO(settings);
    }

    public AttendanceSettingsDTO saveSettings(AttendanceSettingsDTO settingsDTO) {
        AttendanceSettings settings = mapToEntity(settingsDTO);
        settings.setId("1");
        settings = repository.save(settings);
        return mapToDTO(settings);
    }

    private AttendanceDTO mapToDTO(Attendance attendance) {
        //Student student = studentRepository.findById(attendance.getStudentId()).orElse(null);
        AttendanceDTO dto = new AttendanceDTO();
        dto.setStudentId(attendance.getStudentId());
        dto.setStudentName(attendance.getStudentName());
        //dto.setStudentName(student != null ? student.getName() : "Unknown");
        dto.setDate(attendance.getDate());
        dto.setStatus(attendance.getStatus());
        return dto;
    }
    private Attendance mapToEntity(AttendanceDTO dto) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(dto.getStudentId());
        attendance.setStudentName(dto.getStudentName());
        attendance.setDate(dto.getDate());
        attendance.setStatus(dto.getStatus());
        return attendance;
    }
    private AttendanceSettingsDTO mapToDTO(AttendanceSettings settings) {
        AttendanceSettingsDTO dto = new AttendanceSettingsDTO();
        dto.setId(settings.getId());
        dto.setLateThreshold(settings.getLateThreshold());
        dto.setAbsenceThreshold(settings.getAbsenceThreshold());
        return dto;
    }

    private AttendanceSettings mapToEntity(AttendanceSettingsDTO dto) {
        AttendanceSettings settings = new AttendanceSettings();
        settings.setId(dto.getId());
        settings.setLateThreshold(dto.getLateThreshold());
        settings.setAbsenceThreshold(dto.getAbsenceThreshold());
        return settings;
    }
}
