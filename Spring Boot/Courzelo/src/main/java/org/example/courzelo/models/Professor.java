package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "professors")
public class Professor {
    private String professorId;
    private String name;
    private Set<Timeslot> unavailableTimeSlots;

}
