package com.limtic.lab.controller;

import com.limtic.lab.model.Event;
import com.limtic.lab.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    // -------------------- CRUD --------------------
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------- ENROLL USER --------------------
    @PostMapping("/{id}/enroll/{userId}")
    public Event enrollUser(@PathVariable String id, @PathVariable String userId) {
        return eventService.enrollUser(id, userId);
    }

    // -------------------- COUNT --------------------
    @GetMapping("/count")
    public long countEvents() {
        return eventService.countEvents();
    }

    // -------------------- SEARCH / FILTER --------------------
    @GetMapping("/search/name")
    public List<Event> findByName(@RequestParam String name) {
        return eventService.findByName(name);
    }

    @GetMapping("/search/location")
    public List<Event> findByLocation(@RequestParam String location) {
        return eventService.findByLocation(location);
    }

    @GetMapping("/search/status")
    public List<Event> findByStatus(@RequestParam String status) {
        return eventService.findByStatus(status);
    }

    @GetMapping("/search/budget")
    public List<Event> findByBudget(@RequestParam double min, @RequestParam double max) {
        return eventService.findByBudget(min, max);
    }

    @GetMapping("/search/startAfter")
    public List<Event> findByStartDateAfter(@RequestParam String start) {
        LocalDate date = LocalDate.parse(start);
        return eventService.findByStartDateBetween(date, LocalDate.MAX); // tous après
    }

    @GetMapping("/search/endBefore")
    public List<Event> findByEndDateBefore(@RequestParam String end) {
        LocalDate date = LocalDate.parse(end);
        return eventService.findByStartDateBetween(LocalDate.MIN, date); // tous avant
    }

    @GetMapping("/search/dateRange")
    public List<Event> findByDateRange(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return eventService.findByStartDateBetween(startDate, endDate);
    }

    // -------------------- UPCOMING EVENTS --------------------
    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        return eventService.findUpcomingEvents();
    }
}
