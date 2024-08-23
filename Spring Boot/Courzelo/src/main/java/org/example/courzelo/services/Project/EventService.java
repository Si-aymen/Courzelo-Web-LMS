package org.example.courzelo.services.Project;

import org.example.courzelo.models.ProjectEntities.event.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAllEvents();
    List<Event> getEventsByProjectId(String projectId);
    Optional<Event> getEventById(String id);
    Event saveEvent(Event event);
    void deleteEvent(String id);
    Event updateEvent(String id, Event eventDetails) ;
}
