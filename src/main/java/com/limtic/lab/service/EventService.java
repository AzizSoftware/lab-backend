package com.limtic.lab.service;

import com.limtic.lab.model.Event;
import com.limtic.lab.repository.EventRepository;
import com.limtic.lab.repository.UserRepository;
import com.limtic.lab.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    // -------------------- CRUD --------------------
    public Event createEvent(Event event) {
        if (event.getAvailablePlaces() == null) {
            event.setAvailablePlaces(event.getMaxParticipants());
        }
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

    // -------------------- ENROLL USERS --------------------
    public Event enrollUser(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getEnrolledUsers() == null) {
            event.setEnrolledUsers(new ArrayList<>());
        }

        if (event.getEnrolledUsers().contains(userId)) {
            throw new RuntimeException("User already enrolled");
        }

        if (event.getAvailablePlaces() <= 0) {
            throw new RuntimeException("No available spots");
        }

        event.getEnrolledUsers().add(userId);
        event.setAvailablePlaces(event.getAvailablePlaces() - 1);
        eventRepository.save(event);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getEnrolledEvents() == null) {
                user.setEnrolledEvents(new ArrayList<>());
            }
            if (!user.getEnrolledEvents().contains(eventId)) {
                user.getEnrolledEvents().add(eventId);
                userRepository.save(user);
            }
        } else {
            throw new RuntimeException("User not found");
        }

        return event;
    }
    // -------------------- COUNT --------------------
    public long countEvents() {
        return eventRepository.count();
    }

    // -------------------- SEARCH / FILTER --------------------
    public List<Event> findByName(String name) {
        return eventRepository.findByEventNameContainingIgnoreCase(name);
    }

    public List<Event> findByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Event> findByStatus(String status) {
        return eventRepository.findByStatus(status);
    }

    public List<Event> findByBudget(double min, double max) {
        return eventRepository.findByBudgetBetween(min, max);
    }

    public List<Event> findByStartDateBetween(LocalDate start, LocalDate end) {
        return eventRepository.findByStartDateBetween(start, end);
    }

    // -------------------- UPCOMING EVENTS --------------------
    public List<Event> findUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByStartDateGreaterThanEqual(today);
    }
}
