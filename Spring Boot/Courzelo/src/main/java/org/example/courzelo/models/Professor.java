package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Document(collection = "professors")
public class Professor {
    private String id;
    private String name;
    private List<Timeslot> unavailableTimeSlots;

}
