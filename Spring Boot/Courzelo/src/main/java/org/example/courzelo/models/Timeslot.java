package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;

@Data
@Document(collection = "timeslots")
public class Timeslot {
    private DayOfWeek dayOfWeek;
    private Period period;
}
