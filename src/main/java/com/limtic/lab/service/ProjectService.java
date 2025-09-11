package com.limtic.lab.service;

import com.limtic.lab.model.Project;
import com.limtic.lab.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        project.setCreatedAt(new Date());
        project.setAvailableSpots(project.getMaxTeamMembers());
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

    public Project addTeamMember(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getTeamMembers() == null) {
            project.setTeamMembers(new java.util.ArrayList<>());
        }

        if (project.getTeamMembers().contains(userId)) {
            throw new RuntimeException("User already in team");
        }

        if (project.getAvailableSpots() <= 0) {
            throw new RuntimeException("No available spots");
        }

        project.getTeamMembers().add(userId);
        project.setAvailableSpots(project.getAvailableSpots() - 1);
        return projectRepository.save(project);
    }
}
