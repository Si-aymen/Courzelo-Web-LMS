package org.example.courzelo.institution;

import static org.junit.jupiter.api.Assertions.*;


import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.serviceImpls.InstitutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InstitutionServiceImplIntegrationTest {

    @Autowired
    private InstitutionServiceImpl institutionService;

    @Autowired
    private InstitutionRepository institutionRepository;

    private Institution institution;

    @BeforeEach
    void setUp() {
        institution = new Institution();
        institution.setName("Test Institution");
        institution.setCountry("Test Country");
        institution.setAddress("123 Test Address");
        institutionRepository.save(institution);
    }


    @Test
    void testGetInstitutionById() {
        InstitutionResponse response = institutionService.getInstitutionByID(institution.getId()).getBody();
        assertNotNull(response);
        assertEquals(institution.getName(), response.getName());
        assertEquals(institution.getCountry(), response.getCountry());
        assertEquals(institution.getAddress(), response.getAddress());
    }

    @Test
    void testUpdateInstitution() {
        InstitutionRequest request = new InstitutionRequest();
        request.setName("Updated Name");
        request.setCountry("Updated Country");
        request.setAddress("Updated Address");

        institutionService.updateInstitutionInformation(institution.getId(),request,  null);

        Optional<Institution> updatedInstitution = institutionRepository.findById(institution.getId());
        assertTrue(updatedInstitution.isPresent());
        assertEquals("Updated Name", updatedInstitution.get().getName());
        assertEquals("Updated Country", updatedInstitution.get().getCountry());
        assertEquals("Updated Address", updatedInstitution.get().getAddress());
    }

    @Test
    void testDeleteInstitution() {
        institutionService.deleteInstitution(institution.getId());

        Optional<Institution> deletedInstitution = institutionRepository.findById(institution.getId());
        assertFalse(deletedInstitution.isPresent());
    }
}


