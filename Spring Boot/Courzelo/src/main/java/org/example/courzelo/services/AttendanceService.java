package org.example.courzelo.services;

import org.example.courzelo.dto.AttendanceDTO;
import org.example.courzelo.models.Attendance;
import org.example.courzelo.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceDTO> getAttendanceByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO saveAttendance(AttendanceDTO attendanceDTO) {
        Attendance attendance = convertToEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        return convertToDTO(attendance);
    }

    public AttendanceDTO updateAttendance(AttendanceDTO attendanceDTO) {
        Attendance attendance = convertToEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        return convertToDTO(attendance);
    }

    public void deleteAttendance(String id) {
        attendanceRepository.deleteById(id);
    }

    public int getAttendanceCount(String studentId) {
        return attendanceRepository.countByStudentIdAndPresentTrue(studentId);
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudentId());
        dto.setSessionId(attendance.getSessionId());
        dto.setPresent(attendance.isPresent());
        dto.setLateArrival(attendance.isLateArrival());
        dto.setAbsence(attendance.isAbsence());
        return dto;
    }

    private Attendance convertToEntity(AttendanceDTO dto) {
        Attendance attendance = new Attendance();
        attendance.setId(dto.getId());
        attendance.setStudentId(dto.getStudentId());
        attendance.setSessionId(dto.getSessionId());
        attendance.setPresent(dto.isPresent());
        attendance.setLateArrival(dto.isLateArrival());
        attendance.setAbsence(dto.isAbsence());
        return attendance;
    }
}
