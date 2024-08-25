package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.dto.responses.institution.SimplifiedCourseResponse;

import java.util.List;


@Data
@Builder
public class UserEducationResponse {
    private String institutionID;
    private String institutionName;
    private List<SimplifiedCourseResponse> courses;
    private String groupID;

}
