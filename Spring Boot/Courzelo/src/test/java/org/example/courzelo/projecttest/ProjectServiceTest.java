package org.example.courzelo.projecttest;

import org.example.courzelo.dto.ProjectDTO.projectdto.ProjectDTO;
import org.example.courzelo.dto.ProjectDTO.projectdto.TaskDTO;
import org.example.courzelo.models.ProjectEntities.project.*;
import org.example.courzelo.repositories.ProjectRepo.GroupProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.ProjectRepo;
import org.example.courzelo.repositories.ProjectRepo.TasksRepo;
import org.example.courzelo.serviceImpls.Project.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springdoc.api.OpenApiResourceNotFoundException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private GroupProjectRepo groupProjectRepo;

    @Mock
    private TasksRepo tasksRepo;

    @Mock
    private ModelMapper modelMapper;

    private ProjectDTO projectDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample ProjectDTO object
        projectDTO = new ProjectDTO();
        projectDTO.setId("1");
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setDifficulty(Difficulty.Medium);
        projectDTO.setValidate(Validate.NotValidate);
        projectDTO.setSpecialities(Set.of(Speciality.BD,Speciality.IA));
        projectDTO.setDatedebut(new Date());
        projectDTO.setDeadline(new Date());
        projectDTO.setNumber(10);
        projectDTO.setCreatedBy("User1");
        projectDTO.setHasGroupProject(false);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setId("101");
        taskDTO1.setName("task1");
        taskDTO1.setStatus("Done");
        taskDTOs.add(taskDTO1);

        projectDTO.setTasks(taskDTOs);

        // Create a sample Project entity
        project = new Project();
        project.setId("1");
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setDifficulty(Difficulty.Easy);
        project.setValidate(Validate.Validate);
        project.setSpecialities(Set.of(Speciality.BD));
        project.setDatedebut(new Date());
        project.setDeadline(new Date());
        project.setNumber(10);
        project.setCreatedBy("User1");
        project.setHasGroupProject(false);

        List<Tasks> tasks = new ArrayList<>();
        Tasks task1 = new Tasks();
        task1.setId("101");
        task1.setName("task1");
        task1.setStatus(Status.Done);
        tasks.add(task1);

        project.setTasks(tasks);
    }

    @Test
    void testSaveProject() {
        // Mock the behavior of ModelMapper to map ProjectDTO to Project entity
        when(modelMapper.map(projectDTO, Project.class)).thenReturn(project);

        // Mock the behavior of ProjectRepo to save the Project entity
        when(projectRepo.save(project)).thenReturn(project);

        // Mock the behavior of ModelMapper to map the saved Project entity back to ProjectDTO
        when(modelMapper.map(project, ProjectDTO.class)).thenReturn(projectDTO);

        // Mock the behavior of tasksRepo to save the list of tasks
        when(tasksRepo.saveAll(any(List.class))).thenReturn(project.getTasks());

        // Call the saveProject() method
        Project savedProject = projectService.saveProject(project);

        // Verify that the returned Project has the expected values
        assertEquals(projectDTO.getId(), savedProject.getId());
        assertEquals(projectDTO.getName(), savedProject.getName());
        assertEquals(projectDTO.getDescription(), savedProject.getDescription());

        // Verify the tasks list
        assertNotNull(savedProject.getTasks());
        assertEquals(1, savedProject.getTasks().size());
        assertEquals("101", savedProject.getTasks().get(0).getId());
        assertEquals("task1", savedProject.getTasks().get(0).getName());
        assertEquals(Status.Done, savedProject.getTasks().get(0).getStatus());

        // Print out the saved ProjectDTO and the mocked Project object for debugging purposes
        System.out.println("Saved ProjectDTO: " + projectDTO);
        System.out.println("Mocked Project: " + project);

        // Verify that the save method of projectRepo was called twice
        verify(projectRepo, times(2)).save(project);

        // Verify that the saveAll method of tasksRepo was called once
        verify(tasksRepo, times(1)).saveAll(any(List.class));
    }
    @Test
    void testGetProject() {
        // Create a sample list of projects
        Project project1 = new Project();
        project1.setId("1");
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId("2");
        project2.setName("Project 2");

        List<Project> projects = List.of(project1, project2);

        // Mock the repository behaviors
        when(projectRepo.findAll()).thenReturn(projects);
        when(groupProjectRepo.existsByProjectId("1")).thenReturn(true);
        when(groupProjectRepo.existsByProjectId("2")).thenReturn(false);

        // Call the GetProject() method
        List<Project> result = projectService.GetProject();

        // Verify the results
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify the properties for the first project
        Project resultProject1 = result.get(0);
        assertEquals("1", resultProject1.getId());
        assertTrue(resultProject1.isHasGroupProject());

        // Verify the properties for the second project
        Project resultProject2 = result.get(1);
        assertEquals("2", resultProject2.getId());
        assertFalse(resultProject2.isHasGroupProject());

        // Print out the result for debugging purposes
        System.out.println("Result Projects: " + result);

        // Verify that the appropriate repository methods were called
        verify(projectRepo, times(1)).findAll();
        verify(groupProjectRepo, times(2)).existsByProjectId(anyString());
    }


    @Test
    void testUpdateProject() {
        // Create a sample Project entity
        Project project = new Project();
        project.setId("1");
        project.setName("Updated Project");
        project.setDescription("Updated Description");
        project.setDifficulty(Difficulty.Hard);
        project.setValidate(Validate.Validate);
        project.setSpecialities(Set.of(Speciality.IA));
        project.setDatedebut(new Date());
        project.setDeadline(new Date());
        project.setNumber(20);
        project.setCreatedBy("User2");
        project.setHasGroupProject(true);

        // Mock the behavior of ProjectRepo to save the Project entity
        when(projectRepo.save(project)).thenReturn(project);

        // Call the updateProject() method
        Project updatedProject = projectService.updateProject(project);

        // Verify that the returned Project has the expected values
        assertNotNull(updatedProject);
        assertEquals(project.getId(), updatedProject.getId());
        assertEquals(project.getName(), updatedProject.getName());
        assertEquals(project.getDescription(), updatedProject.getDescription());
        assertEquals(project.getDifficulty(), updatedProject.getDifficulty());
        assertEquals(project.getValidate(), updatedProject.getValidate());
        assertEquals(project.getSpecialities(), updatedProject.getSpecialities());
        assertEquals(project.getDatedebut(), updatedProject.getDatedebut());
        assertEquals(project.getDeadline(), updatedProject.getDeadline());
        assertEquals(project.getNumber(), updatedProject.getNumber());
        assertEquals(project.getCreatedBy(), updatedProject.getCreatedBy());
        assertEquals(project.isHasGroupProject(), updatedProject.isHasGroupProject());

        // Print out the updated Project for debugging purposes
        System.out.println("Updated Project: " + updatedProject);

        // Verify that the save method of projectRepo was called once
        verify(projectRepo, times(1)).save(project);
    }

    @Test
    void testUpdateProjectValidationStatus_Success() {
        // Create a sample Project entity
        Project existingProject = new Project();
        existingProject.setId("1");
        existingProject.setName("Existing Project");
        existingProject.setValidate(Validate.NotValidate);

        // Define the new validation status
        Validate newStatus = Validate.Validate;

        // Mock the repository behavior
        when(projectRepo.findById("1")).thenReturn(Optional.of(existingProject));
        when(projectRepo.save(existingProject)).thenReturn(existingProject);

        // Call the updateProjectValidationStatus() method
        Project updatedProject = projectService.updateProjectValidationStatus("1", newStatus);

        // Verify the updated validation status
        assertNotNull(updatedProject);
        assertEquals("1", updatedProject.getId());
        assertEquals(newStatus, updatedProject.getValidate());

        // Print out the updated Project for debugging purposes
        System.out.println("Updated Project: " + updatedProject);

        // Verify that the findById() and save() methods of projectRepo were called once
        verify(projectRepo, times(1)).findById("1");
        verify(projectRepo, times(1)).save(existingProject);
    }

    @Test
    void testUpdateProjectValidationStatus() {
        // Define the project ID that does not exist
        String nonExistentProjectId = "999";

        // Mock the repository behavior
        when(projectRepo.findById(nonExistentProjectId)).thenReturn(Optional.empty());

        // Call the updateProjectValidationStatus() method and expect an exception
        Exception exception = assertThrows(OpenApiResourceNotFoundException.class, () ->
                projectService.updateProjectValidationStatus(nonExistentProjectId, Validate.Validate)
        );

        // Verify the exception message
        assertEquals("Project not found with id " + nonExistentProjectId, exception.getMessage());

        // Verify that the findById() method of projectRepo was called once
        verify(projectRepo, times(1)).findById(nonExistentProjectId);
        // Verify that save() method was not called
        verify(projectRepo, never()).save(any(Project.class));
    }

    @Test
    void testCheckAndUpdateProjectStatus() {
        // Create sample projects
        Project project1 = new Project();
        project1.setId("1");
        project1.setName("Project 1");
        project1.setDeadline(new Date(System.currentTimeMillis() - 100000)); // Past deadline
        project1.setValidate(Validate.NotValidate);

        Project project2 = new Project();
        project2.setId("2");
        project2.setName("Project 2");
        project2.setDeadline(new Date(System.currentTimeMillis() + 100000)); // Future deadline
        project2.setValidate(Validate.NotValidate);

        List<Project> projects = List.of(project1, project2);

        // Mock the repository behavior
        when(projectRepo.findAll()).thenReturn(projects);
        when(projectRepo.save(any(Project.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Call the checkAndUpdateProjectStatus() method
        projectService.checkAndUpdateProjectStatus();

        // Verify that project1's status is updated
        assertEquals(Validate.Project_Done, project1.getValidate());

        // Verify that project2's status is not updated
        assertEquals(Validate.NotValidate, project2.getValidate());

        // Print out the projects for debugging purposes
        System.out.println("Project 1: " + project1);
        System.out.println("Project 2: " + project2);

        // Verify that the save() method of projectRepo was called once for project1
        verify(projectRepo, times(1)).save(project1);
        // Verify that the save() method of projectRepo was not called for project2
        verify(projectRepo, never()).save(project2);
    }



}
