package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class EvaluationDTO {
    private String id;
    private String title;
    private String description;
    private Date date;
    private String teacherId;
    private List<QuestionDTO> questions;


}
