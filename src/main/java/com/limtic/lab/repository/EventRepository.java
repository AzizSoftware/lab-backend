package com.limtic.lab.repository;

import com.limtic.lab.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByEventName(String eventName);
}
