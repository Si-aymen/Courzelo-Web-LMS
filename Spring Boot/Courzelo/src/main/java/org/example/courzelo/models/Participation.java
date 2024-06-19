package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "participation")
@Data
public class Participation {
    @Id
    private String id;
    private String studentId;
    private int score;
}
