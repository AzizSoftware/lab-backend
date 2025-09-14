package com.limtic.lab.controller;

import com.limtic.lab.model.RoleEnum;
import com.limtic.lab.model.User;
import com.limtic.lab.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController: REST endpoints for administrative operations.
 * Handles approving/declining users, updating roles, viewing users, and generating reports.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    // -------------------- USER MANAGEMENT --------------------

    /**
     * Get all registered users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Approve a pending user registration and assign a role.
     * @param userId the user's ID
     * @param role the role to assign (RoleEnum)
     */
    @PutMapping("/users/{userId}/approve")
    public ResponseEntity<User> approveUser(
            @PathVariable String userId,
            @RequestParam RoleEnum role
    ) {
        User approvedUser = adminService.approveUser(userId, role);
        return ResponseEntity.ok(approvedUser);
    }

    /**
     * Decline a pending user registration.
     */
    @PutMapping("/users/{userId}/decline")
    public ResponseEntity<User> declineUser(@PathVariable String userId) {
        User declinedUser = adminService.declineUser(userId);
        return ResponseEntity.ok(declinedUser);
    }

    /**
     * Update the role of an existing user.
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable String userId,
            @RequestParam RoleEnum role
    ) {
        User updatedUser = adminService.updateUserRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Count users by a specific role.
     */
    @GetMapping("/users/count")
    public ResponseEntity<Long> countUsersByRole(@RequestParam RoleEnum role) {
        long count = adminService.countUsersByRole(role);
        return ResponseEntity.ok(count);
    }

    // -------------------- FILE MANAGEMENT --------------------

    /**
     * Count total uploaded files.
     */
    @GetMapping("/files/count")
    public ResponseEntity<Long> countFiles() {
        long totalFiles = adminService.countFiles();
        return ResponseEntity.ok(totalFiles);
    }

    // -------------------- PROJECT & EVENT MANAGEMENT --------------------
    // You can add endpoints here to fetch or count projects/events if needed

    // -------------------- REPORTING --------------------

    /**
     * Generate a summary report of users, files, projects, events.
     */
    @GetMapping("/report/summary")
    public ResponseEntity<String> generateSummaryReport() {
        String report = adminService.generateSummaryReport();
        return ResponseEntity.ok(report);
    }
}
