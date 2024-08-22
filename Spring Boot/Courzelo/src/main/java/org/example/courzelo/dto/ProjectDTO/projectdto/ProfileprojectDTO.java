package org.example.courzelo.dto.ProjectDTO.projectdto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.dto.ProjectDTO.projectdto.GroupProjectDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileprojectDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String role;
    private String speciality;
    private List<GroupProjectDTO> groupProjectsAsMember;
}
