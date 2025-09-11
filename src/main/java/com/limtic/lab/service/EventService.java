package com.limtic.lab.service;

import com.limtic.lab.model.Event;
import com.limtic.lab.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        event.setCreatedAt(new Date());
        event.setAvailablePlaces(event.getMaxParticipants());
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(String id, Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setEventName(updatedEvent.getEventName());
        event.setLocation(updatedEvent.getLocation());
        event.setBudget(updatedEvent.getBudget());
        event.setMaxParticipants(updatedEvent.getMaxParticipants());
        event.setAvailablePlaces(updatedEvent.getAvailablePlaces());
        event.setStatus(updatedEvent.getStatus());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());
        event.setDescription(updatedEvent.getDescription());
        event.setImage(updatedEvent.getImage());
        event.setImagePath(updatedEvent.getImagePath());
        return eventRepository.save(event);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public Event enrollUser(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getEnrolledUsers() == null) {
            event.setEnrolledUsers(new java.util.ArrayList<>());
        }

        if (event.getEnrolledUsers().contains(userId)) {
            throw new RuntimeException("User already enrolled");
        }

        if (event.getAvailablePlaces() <= 0) {
            throw new RuntimeException("No available spots");
        }

        event.getEnrolledUsers().add(userId);
        event.setAvailablePlaces(event.getAvailablePlaces() - 1);
        return eventRepository.save(event);
    }
}
