package com.limtic.lab.service;

import com.limtic.lab.model.Project;
import com.limtic.lab.model.User;
import com.limtic.lab.repository.ProjectRepository;
import com.limtic.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // -------------------- CRUD --------------------
    public Project createProject(Project project) {
        if (project.getAvailableSpots() == null) {
            project.setAvailableSpots(project.getMaxTeamMembers());
        }
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(String id, Project updatedProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setProjectName(updatedProject.getProjectName());
        project.setDescription(updatedProject.getDescription());
        project.setStatus(updatedProject.getStatus());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());
        project.setBudget(updatedProject.getBudget());
        project.setMaxTeamMembers(updatedProject.getMaxTeamMembers());
        project.setAvailableSpots(updatedProject.getAvailableSpots());
        project.setImage(updatedProject.getImage());
        project.setImagePath(updatedProject.getImagePath());
        return projectRepository.save(project);
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    // -------------------- TEAM MANAGEMENT --------------------
    public Project addTeamMember(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getTeamMembers() == null) {
            project.setTeamMembers(new ArrayList<>());
        }

        if (project.getTeamMembers().contains(userId)) {
            throw new RuntimeException("User already in team");
        }

        if (project.getAvailableSpots() <= 0) {
            throw new RuntimeException("No available spots");
        }

        project.getTeamMembers().add(userId);
        project.setAvailableSpots(project.getAvailableSpots() - 1);
        projectRepository.save(project);

        // Update user's enrolledProjects
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getEnrolledProjects() == null) {
                user.setEnrolledProjects(new ArrayList<>());
            }
            if (!user.getEnrolledProjects().contains(projectId)) {
                user.getEnrolledProjects().add(projectId);
                userRepository.save(user);
            }
        } else {
            throw new RuntimeException("User not found");
        }

        return project;
    }

    // -------------------- COUNT --------------------
    public long countProjects() {
        return projectRepository.count();
    }

    // -------------------- SEARCH / FILTER --------------------
    public List<Project> findByName(String name) {
        return projectRepository.findByProjectNameContainingIgnoreCase(name);
    }

    public List<Project> findByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    public List<Project> findByBudget(double min, double max) {
        return projectRepository.findByBudgetBetween(min, max);
    }

    public List<Project> findByStartDateAfter(LocalDate start) {
        return projectRepository.findByStartDateAfter(start);
    }

    public List<Project> findByEndDateBefore(LocalDate end) {
        return projectRepository.findByEndDateBefore(end);
    }

    public List<Project> findByDateRange(LocalDate start, LocalDate end) {
        return projectRepository.findByStartDateBetween(start, end);
    }

    // -------------------- UPCOMING PROJECTS --------------------
    public List<Project> findUpcomingProjects() {
        LocalDate today = LocalDate.now();
        return projectRepository.findByStartDateGreaterThanEqual(today);
    }
}