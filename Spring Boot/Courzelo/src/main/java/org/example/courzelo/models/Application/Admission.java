package org.example.courzelo.models.Application;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document

public class Admission {
    @Id
    private String id;

    private String title;
    private String description;
    private int places;
    private int waiting;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime starDate ;

    @DBRef
    private User user;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate ;

    @DBRef
    private Institution institution;
    @Enumerated(EnumType.STRING)
    private AdmissionStatus status=AdmissionStatus.Open;
}
