package com.limtic.lab.repository;

import com.limtic.lab.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    // -------------------- DASHBOARD / UPCOMING --------------------
    List<Event> findByStartDateGreaterThanEqual(LocalDate date);

    // -------------------- SEARCH / FILTER --------------------
    List<Event> findByEventNameContainingIgnoreCase(String eventName);
    List<Event> findByLocationContainingIgnoreCase(String location);
    List<Event> findByStatus(String status);
    List<Event> findByBudgetBetween(double min, double max);
    List<Event> findByStartDateBetween(LocalDate start, LocalDate end);

    // -------------------- COUNT --------------------
    long count();
}
