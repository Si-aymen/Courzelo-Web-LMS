package org.example.courzelo.dto.responses.institution;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplifiedCourseResponse {
    private String courseID;
    private String courseName;
}
