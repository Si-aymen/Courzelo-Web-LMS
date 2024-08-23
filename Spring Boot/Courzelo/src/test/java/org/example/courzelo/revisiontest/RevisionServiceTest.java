package org.example.courzelo.revisiontest;


import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.example.courzelo.repositories.RevisionRepo.RevisionRepository;
import org.example.courzelo.serviceImpls.Revision.RevisionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RevisionServiceTest {

    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private RevisionServiceImpl revisionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRevisions() {
        Revision revision = new Revision();
        when(revisionRepository.findAll()).thenReturn(Collections.singletonList(revision));
        var result = revisionService.getAllRevisions();
        System.out.println("testGetAllRevisions result size: " + result.size());
        assertEquals(1, result.size());
    }

    @Test
    void testCreateRevision() {
        Revision revision = new Revision();
        when(revisionRepository.save(revision)).thenReturn(revision);
        var result = revisionService.createRevision(revision);
        System.out.println("testCreateRevision result: " + result);
        assertEquals(revision, result);
    }

    @Test
    void testGetRevisionById() {
        Revision revision = new Revision();
        when(revisionRepository.findById("1")).thenReturn(Optional.of(revision));
        var result = revisionService.getRevisionById("1");
        System.out.println("testGetRevisionById result: " + result);
        assertEquals(revision, result);
    }

    @Test
    void testUpdateRevision() {
        Revision existingRevision = new Revision();
        Revision updatedRevision = new Revision();
        when(revisionRepository.existsById("1")).thenReturn(true);
        when(revisionRepository.save(existingRevision)).thenReturn(updatedRevision);

        existingRevision.setId("1");
        var result = revisionService.updateRevision("1", existingRevision);
        System.out.println("testUpdateRevision result: " + result);
        assertEquals(updatedRevision, result);
    }

    @Test
    void testDeleteRevision() {
        when(revisionRepository.existsById("1")).thenReturn(true);
        doNothing().when(revisionRepository).deleteById("1");

        var result = revisionService.deleteRevision("1");
        System.out.println("testDeleteRevision result: " + result);
        assertTrue(result);
        verify(revisionRepository, times(1)).deleteById("1");
    }


}