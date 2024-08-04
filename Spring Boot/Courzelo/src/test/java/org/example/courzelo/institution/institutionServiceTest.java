package org.example.courzelo.institution;

import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserEducation;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.InstitutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class institutionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private InstitutionServiceImpl institutionService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testAddUserToInstitution() {
        // Arrange
        User user = new User(
                "Test Email",
                "Test name",
                "Test lastname",
                new Date(),
                "male",
                "tunisia",
                "Test password",
                Role.STUDENT
        );
        Institution institution = new Institution(
                "Test Institution",
                "Test Slogan",
                "Test country",
                "Test address",
                "Test description",
                "Test website"
        );
        when(userRepository.save(user)).thenReturn(user);
        when(institutionRepository.save(institution)).thenReturn(institution);
        // Act & Assert for ADMIN role
        institutionService.addUserToInstitution(user, institution, Role.ADMIN);
        assert institution.getAdmins().contains(user);
        verify(institutionRepository, times(1)).save(institution);

        // Act & Assert for TEACHER role
        institutionService.addUserToInstitution(user, institution, Role.TEACHER);
        assert institution.getTeachers().contains(user);
        verify(institutionRepository, times(2)).save(institution);

        // Act & Assert for STUDENT role
        institutionService.addUserToInstitution(user, institution, Role.STUDENT);
        assert institution.getStudents().contains(user);
        verify(institutionRepository, times(3)).save(institution);
    }
}
