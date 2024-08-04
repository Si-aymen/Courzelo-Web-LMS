package org.example.courzelo.models.institution;

import lombok.Builder;
import lombok.Data;
import org.example.courzelo.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "courses")
public class Course {
    @Id
    private String id;
    private String name;
    private String description;
    private int credit;
    @DBRef
    private List<User> teachers=new ArrayList<>();
    @DBRef
    private List<User> students = new ArrayList<>();
    private List<CoursePost> posts=new ArrayList<>();
    @DBRef
    private Institution institution;
}
