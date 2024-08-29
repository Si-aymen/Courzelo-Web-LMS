package org.example.courzelo.services.Project;

import org.example.courzelo.models.ProjectEntities.project.Status;
import org.example.courzelo.models.ProjectEntities.project.Tasks;


import java.util.List;

public interface ITaskService {

    void moveTask(String id, Status newStatus);
    List<Tasks> getTasksByProjectId(String projectId);
    List<Tasks> getTasksByStatus(Status status);

}
