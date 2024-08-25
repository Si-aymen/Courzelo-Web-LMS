package org.example.courzelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ChromosomeDTO {
    private List<GeneDTO> genes;
    private double fitnessScore;

}
