package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "academic")
@Data
public class Academic {
    @Id
    private String id;
    private String studentId;
    private double score;
}
