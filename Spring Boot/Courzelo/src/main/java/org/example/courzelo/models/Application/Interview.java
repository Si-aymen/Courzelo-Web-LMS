package org.example.courzelo.models.Application;

import jakarta.persistence.Id;
import lombok.Data;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Interview {
    @Id
    private String id;

    @DBRef
    private User interviewer;

    private List<String> interviewee;

    @DBRef
    private Admission admission;
}
