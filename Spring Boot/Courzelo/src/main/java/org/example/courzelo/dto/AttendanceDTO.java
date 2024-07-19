package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AttendanceDTO {
    private String id;
    private String studentEmail;
    private String sessionId;
    private boolean present;
    private boolean lateArrival;
    private boolean absence;
}
