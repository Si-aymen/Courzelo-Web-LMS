package org.example.courzelo.serviceImpls.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.ProjectEntities.project.Project;
import org.example.courzelo.models.ProjectEntities.project.Tasks;
import org.example.courzelo.models.ProjectEntities.project.Validate;
import org.example.courzelo.repositories.ProjectRepo.GroupProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.ProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.TasksRepo;
import org.example.courzelo.services.Project.IProjectService;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements IProjectService {
    private final ProjectRepo projectRepo;
    private final GroupProjectRepo groupProjectRepo;
    private final TasksRepo tasksRepo;

    @Override
    public Project saveProject(Project project) {

        List<Tasks> tasksList = project.getTasks();

        if (tasksList != null && !tasksList.isEmpty()) {

            tasksList = tasksRepo.saveAll(tasksList);
        }

        Project savedProject = projectRepo.save(project);

        final Project finalSavedProject = savedProject;


        tasksList.forEach(task -> {
            task.setProject(finalSavedProject);
            tasksRepo.save(task); // Save each task individually
        });


        savedProject.setTasks(tasksList);

        return projectRepo.save(savedProject);
    }

    @Override
    public List<Project> GetProject() {
        List<Project> projects = projectRepo.findAll();
        for (Project project : projects) {
            boolean hasGroupProject = groupProjectRepo.existsByProjectId(project.getId());
            project.setHasGroupProject(hasGroupProject);

            // Fetch tasks associated with the project
            List<Tasks> tasks = tasksRepo.findTasksByProject(project);
            project.setTasks(tasks);
        }
        return projects;
    }


    @Override
    public void removeProject(String id) {
        projectRepo.deleteById(id);
    }

    @Override
    public Project updateProject( Project project)
    {
        return projectRepo.save(project);
    }

    @Override
    public Project getById(String id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no  id " + id));
    }

    @Override
    public Project updateProjectValidationStatus(String projectId, Validate validate) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setValidate(validate);
            return projectRepo.save(project);
        } else {
            throw new OpenApiResourceNotFoundException("Project not found with id " + projectId);
        }
    }

    @Override
    public void checkAndUpdateProjectStatus() {
        List<Project> projects = projectRepo.findAll();
        Date now = new Date();
        for (Project project : projects) {
            if (project.getDeadline().before(now) || project.getDeadline().equals(now)) {
                project.setValidate(Validate.Project_Done);
                projectRepo.save(project);
            }
        }
    }



}
