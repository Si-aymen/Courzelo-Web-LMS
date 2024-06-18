package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "evaluations")
@Data
public class Evaluation {
    @Id
    private String id;
    private String title;
    private String description;
    private Date date;
    private String teacherId;
    private List<Question> questions;
}
