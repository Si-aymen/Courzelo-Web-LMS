package org.example.courzelo.models.ProjectEntities.project;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.ProjectEntities.event.Event;
import org.example.courzelo.models.ProjectEntities.publication.Publication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Project")
public class Project {
    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String description;
    @Indexed
    private Difficulty difficulty;
    @Indexed
    private Validate validate = Validate.NotValidate;

    private Set<Speciality> specialities;
    @Indexed
    private Date datedebut;
    @Indexed
    private Date deadline;
    @Indexed
    private int number;
    @Indexed
    private String createdBy;

    @Indexed
    private boolean hasGroupProject;

    @Indexed
    private List<FileMetadata> files;

    @DBRef
    private List<Tasks> tasks;

    @DBRef
    private List<Event> events;

    @DBRef
    private List<Publication> publications;


    @DBRef
    private List<GroupProject> groupProjects;


}
