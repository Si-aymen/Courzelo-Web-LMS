package org.example.courzelo.serviceImpls.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.ProjectEntities.event.Event;
import org.example.courzelo.repositories.ProjectRepo.EventRepository;
import org.example.courzelo.services.Project.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private  final EventRepository eventRepository;
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByProjectId(String projectId) {  // Add this method
        return eventRepository.findByProjectId(projectId);
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
    public Event updateEvent(String id, Event eventDetails) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setTitle(eventDetails.getTitle());
            event.setStart(eventDetails.getStart());
            event.setEnd(eventDetails.getEnd());
            event.setNotes(eventDetails.getNotes());
            event.setColor(eventDetails.getColor());
            return eventRepository.save(event);
        } else {
            throw new RuntimeException("Event not found");
        }
    }
}

