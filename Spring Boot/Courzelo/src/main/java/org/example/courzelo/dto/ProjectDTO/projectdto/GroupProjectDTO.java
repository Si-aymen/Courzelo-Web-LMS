package org.example.courzelo.dto.ProjectDTO.projectdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupProjectDTO {
    private String id;
    private String name;
    private ProjectDTO project;
    private List<ProfileprojectDTO> students;
    private List<TaskDTO> tasks;
}
