package org.example.courzelo.projecttest;

import org.example.courzelo.models.ProjectEntities.event.Event;
import org.example.courzelo.repositories.ProjectRepo.EventRepository;
import org.example.courzelo.serviceImpls.Project.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));
        assertEquals(1, eventService.getAllEvents().size());
    }

    @Test
    void testGetEventsByProjectId() {
        Event event = new Event();
        when(eventRepository.findByProjectId("123")).thenReturn(Collections.singletonList(event));
        assertEquals(1, eventService.getEventsByProjectId("123").size());
    }

    @Test
    void testGetEventById() {
        Event event = new Event();
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        assertTrue(eventService.getEventById("1").isPresent());
    }

    @Test
    void testSaveEvent() {
        Event event = new Event();
        when(eventRepository.save(event)).thenReturn(event);
        assertEquals(event, eventService.saveEvent(event));
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(eventRepository).deleteById("1");
        eventService.deleteEvent("1");
        verify(eventRepository, times(1)).deleteById("1");
    }

    @Test
    void testUpdateEvent() {
        Event existingEvent = new Event();
        Event updatedEvent = new Event();
        when(eventRepository.findById("1")).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);

        existingEvent.setTitle("New Title");
        Event result = eventService.updateEvent("1", existingEvent);

        assertEquals(updatedEvent, result);
    }
}
