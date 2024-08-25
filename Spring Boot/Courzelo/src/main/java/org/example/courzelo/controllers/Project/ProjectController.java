package org.example.courzelo.controllers.Project;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.ProjectEntities.Project;
import org.example.courzelo.models.ProjectEntities.Validate;
import org.example.courzelo.services.Project.IProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor

@Tag(name = "project")
public class ProjectController {
    private final IProjectService iProjectService;


    @GetMapping("/listofProjects")
    @PreAuthorize("hasRole('TEACHER')")
    public List<Project> getAllProjects(){
        return iProjectService.GetProject();
    }
    @PostMapping("/addProject")
    @PreAuthorize("hasRole('TEACHER')")
    public Project createProject(@RequestBody Project project) {
        return iProjectService.saveProject(project);
    }

    @PutMapping("/UpdatelProject")
    public Project UpdatelProject(@RequestBody Project project)

    {
        return  iProjectService.updateProject(project);
    }

    @DeleteMapping("DeleteProject/{id}")
    public void deleteProject(@PathVariable("id") String id){
        iProjectService.removeProject(id);
    }

    @GetMapping("getProjectbyid/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Project getById(@PathVariable("id") String id){
        return iProjectService.getById(id);
    }


    @PutMapping("/{projectId}/validate")
    public ResponseEntity<Project> validateProject(@PathVariable String projectId) {
        Project updatedProject = iProjectService.updateProjectValidationStatus(projectId, Validate.Validate);
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/{projectId}/check-status")
    public ResponseEntity<Void> checkProjectStatus() {
        iProjectService.checkAndUpdateProjectStatus();
        return ResponseEntity.noContent().build();
    }


}
