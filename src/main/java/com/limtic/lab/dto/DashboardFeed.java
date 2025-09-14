package com.limtic.lab.dto;

import com.limtic.lab.model.FileDocument;
import com.limtic.lab.model.Project;
import com.limtic.lab.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardFeed {
    private LocalDate date; // current date
    private List<Project> upcomingProjects;
    private List<Event> upcomingEvents;
    private List<FileDocument> latestFiles;
}
