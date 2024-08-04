package org.example.courzelo.models.institution;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CoursePost {
    private String id;
    private String title;
    private List<String> files;
    private String author;
    private Instant created;
}
