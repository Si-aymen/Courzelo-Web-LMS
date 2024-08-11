package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupResponse {
    private String id;
    private String name;
    private String institutionID;
    private List<String> students;
    private List<String> courses;
}
