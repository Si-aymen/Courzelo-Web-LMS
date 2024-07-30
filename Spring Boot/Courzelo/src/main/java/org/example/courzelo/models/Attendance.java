package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Document(collection = "attendance")
@Data
public class Attendance {
    private String studentId;
    private String studentName;
    private String courseId;
    private String className;
    private LocalDate date;
    private AttendanceStatus status;
}
