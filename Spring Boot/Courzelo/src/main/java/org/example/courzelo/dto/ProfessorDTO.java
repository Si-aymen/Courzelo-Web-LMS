package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.Timeslot;

import java.util.List;
import java.util.Set;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ProfessorDTO {
    private String id;
    private String name;
    private List<Timeslot> unavailableTimeSlots;


    public ProfessorDTO(String id, String name) {
this.id = id;
        this.name = name;
    }

    public ProfessorDTO() {

    }
}
