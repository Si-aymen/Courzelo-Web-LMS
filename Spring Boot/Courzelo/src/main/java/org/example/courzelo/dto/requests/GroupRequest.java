package org.example.courzelo.dto.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupRequest {
    private String name;
    private String institutionID;
    private List<String> students;
    private List<String> courses;
}
