package org.example.courzelo.controllers.Revision;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.example.courzelo.services.Revision.IRevisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Revision")
public class RevisionController {

    private final IRevisionService revisionService;

    @GetMapping("/revision")
    public ResponseEntity<List<Revision>> getAllRevisions() {
        List<Revision> revisions = revisionService.getAllRevisions();
        return new ResponseEntity<>(revisions, HttpStatus.OK);
    }

    @PostMapping("/revision")
    public ResponseEntity<Revision> createRevision(@RequestBody Revision revision) {
        Revision createdRevision = revisionService.createRevision(revision);
        return new ResponseEntity<>(createdRevision, HttpStatus.CREATED);
    }

    @GetMapping("/revision/{id}")
    public ResponseEntity<Revision> getRevisionById(@PathVariable("id") String id) {
        Revision revision = revisionService.getRevisionById(id);
        if (revision != null) {
            return new ResponseEntity<>(revision, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/revision/{id}")
    public ResponseEntity<Revision> updateRevision(@PathVariable("id") String id, @RequestBody Revision revision) {
        Revision updatedRevision = revisionService.updateRevision(id, revision);
        if (updatedRevision != null) {
            return new ResponseEntity<>(updatedRevision, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevision(@PathVariable("id") String id) {
        boolean isDeleted = revisionService.deleteRevision(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
}