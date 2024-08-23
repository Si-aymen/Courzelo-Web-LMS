package org.example.courzelo.models.Application;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Application {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Admission admission;

    private Double note;

    private Boolean noted=false;
    @Enumerated(EnumType.STRING)
    private Status status=Status.NOT_REVIEWED;
}
