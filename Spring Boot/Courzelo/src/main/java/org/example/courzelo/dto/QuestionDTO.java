package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class QuestionDTO {
    private String id;
    private String questionText;
    private String questionType;
    private List<AnswerDTO> answers;
    private String questionStatus;

}
