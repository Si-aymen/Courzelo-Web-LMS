package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ProfessorDTO {
    private String professorId;
    private String name;
    private Set<String> unavailableTimeSlots;

    public ProfessorDTO(String professorId, String name, Set<String> unavailableTimeSlots) {
        this.professorId = professorId;
        this.name = name;
        this.unavailableTimeSlots = unavailableTimeSlots;
    }
}
