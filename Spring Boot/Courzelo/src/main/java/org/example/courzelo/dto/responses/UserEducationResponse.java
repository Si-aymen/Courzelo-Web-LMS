package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.dto.responses.institution.SimplifiedCourseResponse;
import org.example.courzelo.models.UserEducation;

import java.util.List;


@Data
public class UserEducationResponse {
    private String institutionID;
    private String institutionName;
    private List<SimplifiedCourseResponse> courses;

    public UserEducationResponse(UserEducation userEducation) {
        this.institutionID = userEducation.getInstitution()!= null ? userEducation.getInstitution().getId() : null;
        this.institutionName = userEducation.getInstitution()!= null ? userEducation.getInstitution().getName() : null;
        this.courses = userEducation.getCourses().stream().map(course -> SimplifiedCourseResponse.builder()
                .courseID(course.getId())
                .courseName(course.getName())
                .build()).toList();
    }
}
