package org.example.courzelo.models.RecruitementEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.User.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "jobOffers")

public class JobOffer {
@Id
private  String id;
    @Indexed
    private String title;
    @Indexed
    private String description;
    @Indexed
    private String skills;
    @Indexed
    private String speciality;
    @Indexed
    private String experience;
    @Indexed
    private String address;
    @DBRef
    private User recruiter;
    @DBRef
    private List<Candidacy> candidacies;
    @Indexed
    private int matchingScore;
}
