package org.example.courzelo.services.Project;

import org.example.courzelo.models.ProjectEntities.Status;
import org.example.courzelo.models.ProjectEntities.Tasks;


import java.util.List;

public interface ITaskService {

    void moveTask(String id, Status newStatus);
    List<Tasks> getTasksByProjectId(String projectId);
    List<Tasks> getTasksByStatus(Status status);

}
