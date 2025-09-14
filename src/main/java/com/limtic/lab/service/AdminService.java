package com.limtic.lab.service;

import com.limtic.lab.model.Event;

import com.limtic.lab.model.Project;
import com.limtic.lab.model.RoleEnum;
import com.limtic.lab.model.User;
import com.limtic.lab.repository.EventRepository;
import com.limtic.lab.repository.ProjectRepository;
import com.limtic.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * AdminService: provides administrative operations for managing users, projects,
 * events, and generating summary reports.
 *
 * Adjusted to use RoleEnum and status field instead of non-existing methods.
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EventRepository eventRepository;

    // -------------------- USER MANAGEMENT --------------------

    /**
     * Get all registered users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Approve a pending user registration.
     * @param userId the user's ID
     * @param newRole the role to assign (RoleEnum)
     */
    public User approveUser(String userId, RoleEnum newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set role using helper method
        user.setRoleEnum(newRole);

        // Set status to APPROVED
        user.setStatus("APPROVED");

        return userRepository.save(user);
    }

    /**
     * Decline a pending user registration.
     * @param userId the user's ID
     */
    public User declineUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set status to DECLINED
        user.setStatus("DECLINED");

        return userRepository.save(user);
    }

    /**
     * Update user role for an existing user.
     */
    public User updateUserRole(String userId, RoleEnum role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRoleEnum(role);
        return userRepository.save(user);
    }

    /**
     * Count of users with a specific role.
     */
    public long countUsersByRole(RoleEnum role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoleEnum() == role)
                .count();
    }

    // -------------------- FILE MANAGEMENT --------------------

    /**
     * Count total uploaded files across all users.
     */
    public long countFiles() {
        return userRepository.findAll().stream()
                .mapToLong(u -> u.getUploads() != null ? u.getUploads().size() : 0)
                .sum();
    }

    // -------------------- PROJECT MANAGEMENT --------------------

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public long countProjects() {
        return projectRepository.count();
    }

    // -------------------- EVENT MANAGEMENT --------------------

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public long countEvents() {
        return eventRepository.count();
    }

    // -------------------- REPORTING --------------------

    /**
     * Generate a summary report: counts of users, files, projects, events.
     */
    public String generateSummaryReport() {
        long totalUsers = userRepository.count();
        long totalProjects = projectRepository.count();
        long totalEvents = eventRepository.count();
        long totalFiles = countFiles();

        return "=== Admin Summary Report ===\n" +
                "Total Users: " + totalUsers + "\n" +
                "Total Projects: " + totalProjects + "\n" +
                "Total Events: " + totalEvents + "\n" +
                "Total Files: " + totalFiles + "\n" +
                "Report generated on: " + LocalDate.now();
    }
}
