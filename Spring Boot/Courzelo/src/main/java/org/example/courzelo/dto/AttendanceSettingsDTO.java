package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AttendanceSettingsDTO {
    private String id;
    private int lateThreshold;
    private int absenceThreshold;

}
