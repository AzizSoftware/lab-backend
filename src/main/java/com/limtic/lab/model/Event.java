package com.limtic.lab.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "events")
public class Event {
    @Id
    private String id;

    private String eventName;
    private String location;

    private float budget;
    private Integer maxParticipants;
    private Integer availablePlaces;

    private String status; // e.g. "PLANNED", "ONGOING", "FINISHED"

    private String image;
    private String imagePath;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String description;
    private List<String> enrolledUsers;

    private Date createdAt;
}
