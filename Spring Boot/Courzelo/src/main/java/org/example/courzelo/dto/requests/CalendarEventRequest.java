package org.example.courzelo.dto.requests;

import lombok.Data;

import java.util.Date;
@Data
public class CalendarEventRequest {
        Date startDate;
        Date finishDate;
        String name;
        String color;

}
