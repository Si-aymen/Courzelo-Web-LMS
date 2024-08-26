package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.dto.QuizDTO;

import java.util.List;

@Data
@Builder
public class CourseResponse {
    private String id;
    private String name;
    private String description;
    private int credit;
    private String teacher;
    private List<String> students;
    private String group;
    private String institutionID;
    private List<CoursePostResponse> posts;
    private List<QuizDTO> quizzes;
}
