package org.example.courzelo.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseRequest {
    private String name;
    private String description;
    private int credit;
}
