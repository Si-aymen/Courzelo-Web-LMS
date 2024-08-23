package org.example.courzelo.dto.ProjectDTO.projectdto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.dto.ProjectDTO.publicationdto.PublicationDTO;
import org.example.courzelo.models.ProjectEntities.project.Difficulty;
import org.example.courzelo.models.ProjectEntities.project.Speciality;
import org.example.courzelo.models.ProjectEntities.project.Validate;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private String id;
    private String name;
    private String description;
    private Difficulty difficulty;
    private Validate validate;
    private Set<Speciality> specialities;
    private Date datedebut;
    private Date deadline;
    private int number;
    private String createdBy;
    private boolean hasGroupProject;
    private List<TaskDTO> tasks;
   // private List<FileMetadataDTO> files;

  //  private List<PublicationDTO> publications;
 //   private List<GroupProjectDTO> groupProjects;
}