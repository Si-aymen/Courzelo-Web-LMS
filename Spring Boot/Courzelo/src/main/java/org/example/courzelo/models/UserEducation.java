package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.models.institution.Course;
import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEducation {
    @DBRef
    private Institution institution;
    @DBRef
    private List<Course> courses = new ArrayList<>();
}
