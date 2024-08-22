package org.example.courzelo.controllers.Project;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.ProjectEntities.event.Event;
import org.example.courzelo.models.ProjectEntities.project.Project;
import org.example.courzelo.services.Project.EventService;
import org.example.courzelo.services.Project.IProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Event")
public class EventController {
    private final EventService eventService ;
    private final IProjectService projectService ;
    @GetMapping("/getallevent")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/projectcalendar/{projectId}")
    public List<Event> getEventsByProjectId(@PathVariable String projectId) {
        return eventService.getEventsByProjectId(projectId);
    }

    @GetMapping("geteventbyid/{id}")
    public Event getEventById(@PathVariable String id) {
        return eventService.getEventById(id).orElse(null);
    }

    @PostMapping("/projectcalendar/{projectId}/add")
    public Event saveEvent(@PathVariable String projectId, @RequestBody Event event) {
        System.out.println("Received projectId: " + projectId);
        System.out.println("Received event: " + event);

        Project project = projectService.getById(projectId);
        if (project != null) {
            event.setProject(project);  // Set the project in the event
            Event savedEvent = eventService.saveEvent(event);
            System.out.println("Saved event: " + savedEvent);
            return savedEvent;  // Return the saved event
        } else {
            throw new RuntimeException("Project not found");
        }
    }
    @PutMapping("/updateevent/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("deleteevent/{id}")
    public void deleteEvent(@PathVariable String id) {
        System.out.println("Deleting event with ID: " + id);
        eventService.deleteEvent(id);
    }
}