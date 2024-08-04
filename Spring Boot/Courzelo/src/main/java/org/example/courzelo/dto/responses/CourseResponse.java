package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.models.institution.CoursePost;

import java.util.List;

@Data
@Builder
public class CourseResponse {
    private String id;
    private String name;
    private String description;
    private int credit;
    private List<String> teachers;
    private List<String> students;
    private List<CoursePostResponse> posts;
}
