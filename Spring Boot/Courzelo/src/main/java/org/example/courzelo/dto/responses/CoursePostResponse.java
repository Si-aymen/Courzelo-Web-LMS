package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CoursePostResponse {
    private String id;
    private String title;
    private String author;
    private Instant created;
    private List<byte[]> files;
}
