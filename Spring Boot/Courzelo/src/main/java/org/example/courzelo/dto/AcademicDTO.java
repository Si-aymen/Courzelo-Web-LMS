package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AcademicDTO {
    private String id;
    private String studentId;
    private double score;
}
