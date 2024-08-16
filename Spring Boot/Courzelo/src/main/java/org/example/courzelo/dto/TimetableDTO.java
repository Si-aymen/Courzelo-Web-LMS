package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.DayOfWeek;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class TimetableDTO {
    private DayOfWeek dayOfWeek;
    private String period;
    private String courseName;
    private String professorName;

    public TimetableDTO(DayOfWeek dayOfWeek, String p1, String mathematics, String s) {
        this.dayOfWeek = dayOfWeek;
        this.period = p1;
        this.courseName = mathematics;
        this.professorName = s;
    }

    public TimetableDTO() {

    }

    public void addCourse(String courseId, String professorId, TimeslotDTO timeSlot) {

    }
}
