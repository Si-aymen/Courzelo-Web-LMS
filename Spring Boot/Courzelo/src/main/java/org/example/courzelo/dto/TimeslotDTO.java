package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.courzelo.models.Period;

import java.time.DayOfWeek;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class TimeslotDTO {
    private DayOfWeek dayOfWeek;
    private Period period;

    public TimeslotDTO(DayOfWeek randomDay, Period randomPeriod) {
        this.dayOfWeek = randomDay;
        this.period = randomPeriod;
    }
}
