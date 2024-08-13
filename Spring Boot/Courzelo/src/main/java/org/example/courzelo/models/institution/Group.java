package org.example.courzelo.models.institution;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;
    private String institutionID;
    private List<String> students;
    private List<String> courses;
}
