package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AnswerDTO {
    private String id;
    private String answerText;
    private boolean correct;// QCM questions
}
