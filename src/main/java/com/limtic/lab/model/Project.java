package com.limtic.lab.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "projects")
public class Project {
    @Id
    private String id;

    private String projectName;
    private String description;

    private String status; // "ACTIVE", "COMPLETED", "CANCELLED"

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private float budget;
    private Integer maxTeamMembers;
    private Integer availableSpots;

    private String image;
    private String imagePath;

    private List<String> teamMembers;

    private Date createdAt;
}
