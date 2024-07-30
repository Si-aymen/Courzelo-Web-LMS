package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.AttendanceStatus;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AttendanceDTO {
    private String studentId;
    private String studentName;
    private String courseId;
    private String className;
    private LocalDate date;
    private AttendanceStatus status;
}
