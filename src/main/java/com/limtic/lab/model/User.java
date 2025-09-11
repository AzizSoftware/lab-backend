package com.limtic.lab.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
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
    private String role; // "USER", "ADMIN"

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
}
