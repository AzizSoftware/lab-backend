package com.limtic.lab.service;

import com.limtic.lab.dto.DashboardFeed;
import com.limtic.lab.model.Event;
import com.limtic.lab.model.FileDocument;
import com.limtic.lab.model.Project;
import com.limtic.lab.repository.EventRepository;
import com.limtic.lab.repository.FileRepository;
import com.limtic.lab.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileRepository fileRepository;

    /**
     * Retourne le feed pour le dashboard :
     * - projets à venir
     * - événements à venir
     * - derniers fichiers uploadés
     */
    public DashboardFeed getDashboardFeed(int latestFilesCount) {

        LocalDate today = LocalDate.now();

        // 1️⃣ Upcoming projects
        List<Project> upcomingProjects = projectRepository.findByStartDateGreaterThanEqual(today);

        // 2️⃣ Upcoming events
        List<Event> upcomingEvents = eventRepository.findByStartDateGreaterThanEqual(today);

        // 3️⃣ Latest files uploaded
        List<FileDocument> latestFiles = fileRepository.findAll().stream()
                .filter(f -> f.getUploadedAt() != null)
                .sorted(Comparator.comparing(FileDocument::getUploadedAt).reversed())
                .limit(latestFilesCount)
                .collect(Collectors.toList());

        return new DashboardFeed(today, upcomingProjects, upcomingEvents, latestFiles);
    }
}
