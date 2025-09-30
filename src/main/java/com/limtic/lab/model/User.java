package com.limtic.lab.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    private String role;   // stored as string in DB, but will use RoleEnum in code
    private String status; // "PENDING", "APPROVED", "DECLINED"

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private String imageUrl;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Academic / Professional info
    private String grade;
    private String institute;
    private String lastDiploma;
    private String researchArea;
    private String linkedInUrl;

    // All documents uploaded by this user
    private List<FileDocument> uploads;
    private List<String> enrolledEvents = new ArrayList<>();

    // Enrolled projects (project IDs)
    private List<String> enrolledProjects = new ArrayList<>();
    // ---------------------------
    // Helper methods for enum
    // ---------------------------

    public void setRoleEnum(RoleEnum roleEnum) {
        this.role = roleEnum.name();
    }

    public RoleEnum getRoleEnum() {
        return RoleEnum.valueOf(this.role);
    }
}
