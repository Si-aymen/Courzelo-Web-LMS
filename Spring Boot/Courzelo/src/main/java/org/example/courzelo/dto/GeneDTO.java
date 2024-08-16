package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class GeneDTO {
    private String courseId;
    private String professorId;
    private TimeslotDTO timeSlot;

    public GeneDTO(String courseId, String professorId, TimeslotDTO timeSlot) {
        this.courseId = courseId;
        this.professorId = professorId;
        this.timeSlot = timeSlot;
    }
}
