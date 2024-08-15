package org.example.courzelo.institution;

import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.serviceImpls.InstitutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InstitutionServiceTest {

    @Autowired
    private InstitutionServiceImpl institutionService;

    @Autowired
    private InstitutionRepository institutionRepository;


    @Mock
    private Principal principal;

    private Institution institution;

    @BeforeEach
    void setUp() {
        institutionRepository.deleteAll();  // Clear any existing data

        // Create a sample institution for testing
        institution = new Institution();
        institution.setId("institution-id");
        institution.setName("Test Institution");
        institutionRepository.save(institution);
    }

    @Test
    void addInstitution_successfullyAddsInstitution() {
        // Arrange
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setName("New Institution");

        // Act
        ResponseEntity<StatusMessageResponse> response = institutionService.addInstitution(institutionRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Institution added successfully", response.getBody().getMessage());

        Institution addedInstitution = institutionRepository.findByName("New Institution").orElseThrow();
        assertEquals("New Institution", addedInstitution.getName());
    }

    @Test
    void updateInstitutionInformation_successfullyUpdatesInstitution() {
        // Arrange
        InstitutionRequest institutionRequest = new InstitutionRequest();
        institutionRequest.setName("Updated Institution");

        // Act
        ResponseEntity<HttpStatus> response = institutionService.updateInstitutionInformation(institution.getId(), institutionRequest, principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Institution updatedInstitution = institutionRepository.findById(institution.getId()).orElseThrow();
        assertEquals("Updated Institution", updatedInstitution.getName());
    }

    @Test
    void deleteInstitution_successfullyDeletesInstitution() {
        // Arrange
        // Assuming institution is already saved in setUp()

        // Act
        ResponseEntity<StatusMessageResponse> response = institutionService.deleteInstitution(institution.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Institution deleted successfully", response.getBody().getMessage());

        assertEquals(Optional.empty(), institutionRepository.findById(institution.getId()));
    }

    @Test
    void getInstitutionByID_successfullyRetrievesInstitution() {
        // Arrange
        // Institution is already saved in setUp()

        // Act
        ResponseEntity<InstitutionResponse> response = institutionService.getInstitutionByID(institution.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(institution.getId(), response.getBody().getId());
        assertEquals(institution.getName(), response.getBody().getName());
    }

    @Test
    void getInstitutionByID_throwsNoSuchElementException() {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            institutionService.getInstitutionByID("non-existent-id");
        });
    }
}
