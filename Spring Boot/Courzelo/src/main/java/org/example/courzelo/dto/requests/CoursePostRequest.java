package org.example.courzelo.dto.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
@Data
@Builder
public class CoursePostRequest {
    private String title;
    private String author;
    private Instant created;
    private MultipartFile[] files;
}
