package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attendance")
@Data
public class Attendance {
    @Id
    private String id;
    private String student;
    private String sessionId;
    private boolean present;
    private boolean lateArrival;
    private boolean absence;

}
