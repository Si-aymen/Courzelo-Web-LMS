package org.example.courzelo.models.institution;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CoursePost {
    private String id;
    private String title;
    private String description;
    private List<String> files;
    private Instant created;
    public static class CoursePostBuilder {
        public CoursePostBuilder() {
            this.id = UUID.randomUUID().toString();
        }
    }
}
