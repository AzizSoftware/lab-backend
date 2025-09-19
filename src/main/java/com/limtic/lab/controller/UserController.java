package com.limtic.lab.controller;

import com.limtic.lab.config.AdminCredentialsConfig;
import com.limtic.lab.model.FileDocument;
import com.limtic.lab.model.RoleEnum;
import com.limtic.lab.model.User;
import com.limtic.lab.repository.UserRepository;
import com.limtic.lab.service.UserService;
import com.limtic.lab.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final AdminCredentialsConfig adminConfig;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    // -------------------- AUTH -------------------
     @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        // ------------------ AUTO-APPROVE ADMIN ------------------
        if (user.getEmail().equalsIgnoreCase(adminConfig.getEmail())) {
            user.setRoleEnum(RoleEnum.SUPER_ADMIN);
            user.setStatus("APPROVED"); // ✅ immediately approved
        } else {
            user.setRoleEnum(RoleEnum.USER);
            user.setStatus("PENDING"); // normal users
        }

        userRepository.save(user);

        return ResponseEntity.ok(
            user.getStatus().equals("APPROVED") ?
            "Admin account created and approved." :
            "Registration successful. Pending admin approval."
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        // -------------------- Admin login --------------------
        if (loginRequest.getEmail().equals(adminConfig.getEmail()) &&
            passwordEncoder.matches(loginRequest.getPassword(), adminConfig.getPassword())) {

            String token = jwtUtil.generateToken(adminConfig.getEmail(), adminConfig.getRole());
            return ResponseEntity.ok(token);
        }

        // -------------------- Normal user login --------------------
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .filter(u -> "APPROVED".equals(u.getStatus())) // only approved users
                .map(u -> ResponseEntity.ok(jwtUtil.generateToken(u.getEmail(), u.getRole())))
                .orElse(ResponseEntity.status(401).body("Invalid credentials or not approved"));
    }


    // -------------------- USER INFO --------------------
    @GetMapping("/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @PutMapping("/{email}")
    public User updateUser(@PathVariable String email, @RequestBody User userData) {
        return userService.updateUser(email, userData);
    }

    // -------------------- FILE UPLOAD --------------------
    @PostMapping("/{email}/uploads")
    public User uploadFile(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file,
            @RequestParam String title,
            @RequestParam List<String> authors,
            @RequestParam List<String> affiliations,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
            @RequestParam String abstractText,
            @RequestParam List<String> keywords,
            @RequestParam(required = false) String doi
    ) throws IOException {
        return userService.addFileToUser(
                email, file, title, authors, affiliations, publicationDate, abstractText, keywords, doi
        );
    }

    @PostMapping("/{email}/photo")
    public User uploadProfilePhoto(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Photo cannot be empty");
        return userService.uploadProfilePhoto(email, file);
    }

    @GetMapping("/uploads/{filename:.+}")
    public Resource downloadFile(@PathVariable String filename) {
        Path filePath = userService.getFilePath(filename);
        Resource resource = new PathResource(filePath);

        if (!resource.exists()) {
            throw new IllegalArgumentException("File not found: " + filename);
        }

        return resource;
    }

    @GetMapping("/uploads/recent")
    public List<FileDocument> getRecentUploads(@RequestParam(defaultValue = "7") int days) {
        return userService.getRecentUploads(days);
    }

    // -------------------- ROLE MANAGEMENT --------------------
    @PutMapping("/{email}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable String email,
            @RequestParam String role // e.g., "USER" or "ADMIN"
    ) {
        Optional<User> optionalUser = userService.getByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        user.setRole(role.toUpperCase());
        userService.updateUser(email, user);
        return ResponseEntity.ok(user);
    }
}
