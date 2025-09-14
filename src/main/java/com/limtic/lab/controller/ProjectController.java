package com.limtic.lab.controller;

import com.limtic.lab.model.Project;
import com.limtic.lab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Create
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    // Read all
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable String id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Add team member
    @PostMapping("/{id}/addMember/{userId}")
    public Project addTeamMember(@PathVariable String id, @PathVariable String userId) {
        return projectService.addTeamMember(id, userId);
    }

    // Count
    @GetMapping("/count")
    public long countProjects() {
        return projectService.countProjects();
    }

    // Search & filters
    @GetMapping("/search/name")
    public List<Project> findByName(@RequestParam String name) {
        return projectService.findByName(name);
    }

    @GetMapping("/search/status")
    public List<Project> findByStatus(@RequestParam String status) {
        return projectService.findByStatus(status);
    }

    @GetMapping("/search/budget")
    public List<Project> findByBudget(@RequestParam double min, @RequestParam double max) {
        return projectService.findByBudget(min, max);
    }

    @GetMapping("/search/startAfter")
    public List<Project> findByStartDateAfter(@RequestParam String start) {
        LocalDate date = LocalDate.parse(start);
        return projectService.findByStartDateAfter(date);
    }

    @GetMapping("/search/endBefore")
    public List<Project> findByEndDateBefore(@RequestParam String end) {
        LocalDate date = LocalDate.parse(end);
        return projectService.findByEndDateBefore(date);
    }

    @GetMapping("/search/dateRange")
    public List<Project> findByDateRange(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return projectService.findByDateRange(startDate, endDate);
    }
}
