package org.example.courzelo.controllers.Project;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.ProjectEntities.Status;
import org.example.courzelo.models.ProjectEntities.Tasks;
import org.example.courzelo.services.Project.IProjectService;
import org.example.courzelo.services.Project.ITaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Tasks")
public class TasksController {

    private final ITaskService iTaskService;
    private final IProjectService iProjectService;

    @PutMapping("/{id}/move")
    public void moveTask(@PathVariable String id, @RequestParam Status newStatus) {
        iTaskService.moveTask(id, newStatus);
    }



    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Tasks>> getTasksByProject(@PathVariable String projectId) {
        List<Tasks> tasks = iTaskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/status/{status}")
    public List<Tasks> getTasksByStatus(@PathVariable Status status) {
        return iTaskService.getTasksByStatus(status);
    }


}
