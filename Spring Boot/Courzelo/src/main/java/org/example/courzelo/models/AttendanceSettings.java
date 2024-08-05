package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attendance_settings")
@Data
public class AttendanceSettings {
    private String id;
    private int lateThreshold;
    private int absenceThreshold;
}
