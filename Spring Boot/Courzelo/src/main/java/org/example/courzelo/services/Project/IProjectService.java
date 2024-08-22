package org.example.courzelo.services.Project;


import org.example.courzelo.models.ProjectEntities.Project;
import org.example.courzelo.models.ProjectEntities.Validate;

import java.util.List;

public interface IProjectService {

    List<Project> GetProject();
    Project saveProject(Project project);
    void removeProject (String id );
    Project updateProject(Project project);
    Project getById(String id);
    void checkAndUpdateProjectStatus();
    Project updateProjectValidationStatus(String projectId, Validate validate);
}


