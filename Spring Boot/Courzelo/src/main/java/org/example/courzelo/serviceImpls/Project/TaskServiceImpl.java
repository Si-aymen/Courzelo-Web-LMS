package org.example.courzelo.serviceImpls.Project;

import jakarta.el.MethodNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.ProjectEntities.Status;
import org.example.courzelo.models.ProjectEntities.Tasks;
import org.example.courzelo.repositories.ProjectRepo.TasksRepo;
import org.example.courzelo.services.Project.ITaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {
    private final TasksRepo tasksRepo;

    @Override
    public List<Tasks> getTasksByProjectId(String projectId) {
        return tasksRepo.findByProjectId(projectId);
    }
    @Override
    public void moveTask(String id, Status newStatus) {
        Optional<Tasks> optionalTask = tasksRepo.findById(id);
        if (optionalTask.isPresent()) {
            Tasks task = optionalTask.get();
            task.setStatus(newStatus);
            tasksRepo.save(task);
        } else {
            throw new MethodNotFoundException("Task not found with id: " + id);
        }
    }

    @Override
    // Method to get tasks by status
    public List<Tasks> getTasksByStatus(Status status) {
        return tasksRepo.findByStatus(status);
    }
}
