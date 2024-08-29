package org.example.courzelo.models.ProjectEntities.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "profileproject")
public class Profileproject {
    @Id
    private  String id;
    @Indexed
    private String firstName;
    @Indexed
    private String lastName;

    @Indexed
    private Roleproject role;

    @Indexed
    private Speciality speciality;

    private List<GroupProject> groupProjectsAsMember;



}
