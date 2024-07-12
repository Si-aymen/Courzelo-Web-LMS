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
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Candidacy")

public class Candidacy {
    @Id
    private  String id;
    @Indexed
    private String description;
    @Indexed
    private String skills;
    @Indexed
    private String experience;
    @Indexed
    private String resume_candidacy;
    @DBRef
    private User student;
    @DBRef
    private List<JobOffer> jobOffers;
}
