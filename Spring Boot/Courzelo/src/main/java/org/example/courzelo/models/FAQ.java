package org.example.courzelo.models;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class FAQ {
    @Id
    private String id;

    @Size(max = 255)
    private String Questions;

    @Size(max = 255)
    private String Answers;

}

