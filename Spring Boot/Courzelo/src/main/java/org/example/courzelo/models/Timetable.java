package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;

@Data
@Document(collection = "timetables")
public class Timetable {
    private DayOfWeek dayOfWeek;
    private String period;
    private String courseName;
    private String professorName;
    private String ProfessorId;
    @DBRef
    private Professor professor;
}
