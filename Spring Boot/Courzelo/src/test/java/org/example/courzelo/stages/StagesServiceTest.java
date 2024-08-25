package org.example.courzelo.stages;


import org.example.courzelo.models.Stages;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.StagesRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.StagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StagesServiceTest {

    @Mock
    private StagesRepository stagesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StagesService stagesService;

    private Stages stage;
    private User user;

    @BeforeEach
    void setUp() {
        stage = new Stages();
        stage.setId("1");

        user = new User();
        user.setId("1");
    }

    @Test
    void retrieveAllStages_shouldReturnAllStages() {
        List<Stages> stagesList = List.of(stage);
        when(stagesRepository.findAll()).thenReturn(stagesList);

        List<Stages> result = stagesService.retrieveAllStages();

        assertEquals(stagesList, result);
        verify(stagesRepository, times(1)).findAll();
    }

    @Test
    void retrieveStage_shouldReturnStageWhenFound() {
        when(stagesRepository.findById("1")).thenReturn(Optional.of(stage));

        Stages result = stagesService.retrieveStage("1");

        assertNotNull(result);
        assertEquals(stage, result);
        verify(stagesRepository, times(1)).findById("1");
    }

    @Test
    void retrieveStage_shouldReturnNullWhenNotFound() {
        when(stagesRepository.findById("1")).thenReturn(Optional.empty());

        Stages result = stagesService.retrieveStage("1");

        assertNull(result);
        verify(stagesRepository, times(1)).findById("1");
    }

    @Test
    void addStage_shouldSaveAndReturnStage() {
        when(stagesRepository.save(any(Stages.class))).thenReturn(stage);

        Stages result = stagesService.addStage(stage);

        assertNotNull(result);
        assertEquals(stage, result);
        verify(stagesRepository, times(1)).save(stage);
    }

    @Test
    void removeStage_shouldDeleteStage() {
        doNothing().when(stagesRepository).deleteById("1");

        stagesService.removeStage("1");

        verify(stagesRepository, times(1)).deleteById("1");
    }

    @Test
    void modifyStage_shouldSaveAndReturnStage() {
        when(stagesRepository.save(any(Stages.class))).thenReturn(stage);

        Stages result = stagesService.modifyStage(stage);

        assertNotNull(result);
        assertEquals(stage, result);
        verify(stagesRepository, times(1)).save(stage);
    }

    @Test
    void getNumberOfStage_shouldReturnNumberOfStages() {
        when(stagesRepository.count()).thenReturn(5L);

        Long result = stagesService.GetNumberOfStage();

        assertEquals(5L, result);
        verify(stagesRepository, times(1)).count();
    }

    @Test
    void assignStudentToInternship_shouldAssignStudentWhenFound() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(stagesRepository.findById("1")).thenReturn(Optional.of(stage));

        stagesService.AssignStudentToInternship("1", "1");

        verify(stagesRepository, times(1)).save(stage);
        assertTrue(stage.getUserSet().contains(user));
    }

    @Test
    void assignStudentToInternship_shouldNotAssignWhenStudentNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        stagesService.AssignStudentToInternship("1", "1");

        verify(stagesRepository, never()).save(any(Stages.class));
    }

    @Test
    void assignStudentToInternship_shouldNotAssignWhenStageNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(stagesRepository.findById("1")).thenReturn(Optional.empty());

        stagesService.AssignStudentToInternship("1", "1");

        verify(stagesRepository, never()).save(any(Stages.class));
    }
}
