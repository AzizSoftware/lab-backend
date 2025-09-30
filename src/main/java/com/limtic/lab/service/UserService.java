package com.limtic.lab.service;

import com.limtic.lab.model.FileDocument;
import com.limtic.lab.model.Project;
import com.limtic.lab.model.User;
import com.limtic.lab.repository.EventRepository;
import com.limtic.lab.repository.FileRepository;
import com.limtic.lab.repository.ProjectRepository;
import com.limtic.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.limtic.lab.model.Event;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EventRepository eventRepository;


    @Autowired
    private FileTypeService fileTypeService;

    private final Path uploadDir = Paths.get("uploads");

    public UserService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory", e);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<String> getAllUserEmails() {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String email, User userData) {
        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).createdAt(LocalDate.now()).build());

        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setDateOfBirth(userData.getDateOfBirth());
        user.setPhone(userData.getPhone());
        user.setImageUrl(userData.getImageUrl());

        user.setGrade(userData.getGrade());
        user.setInstitute(userData.getInstitute());
        user.setLastDiploma(userData.getLastDiploma());
        user.setResearchArea(userData.getResearchArea());
        user.setLinkedInUrl(userData.getLinkedInUrl());

        user.setUpdatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    public User addFileToUser(
        String email,
        MultipartFile file,
        String title,
        List<String> authors,
        List<String> affiliations,
        LocalDate publicationDate,
        String abstractText,
        List<String> keywords,
        String doi,
        String fileType
    ) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        String uniqueFilename = UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(uniqueFilename);
        Files.write(filePath, file.getBytes());

        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).createdAt(LocalDate.now()).build());

        FileDocument fileDoc = new FileDocument();
        fileDoc.setId(UUID.randomUUID().toString());
        fileDoc.setFilename(uniqueFilename); // Set unique filename
        fileDoc.setFileType(fileType != null ? fileType.toLowerCase() : "other"); // Set fileType
        fileDoc.setTitle(title != null ? title : originalFilename);
        fileDoc.setAuthors(authors);
        fileDoc.setAffiliations(affiliations);
        fileDoc.setPublicationDate(publicationDate);
        fileDoc.setAbstractText(abstractText);
        fileDoc.setKeywords(keywords);
        fileDoc.setDoi(doi != null ? doi : UUID.randomUUID().toString());
        fileDoc.setOwnerId(user.getId());
        fileDoc.setUploadedAt(LocalDate.now());

        // Add custom fileType to the list
        fileTypeService.addFileType(fileType);

        // Save in files collection
        fileRepository.save(fileDoc);

        // Add to user's uploads
        if (user.getUploads() == null) {
            user.setUploads(new ArrayList<>());
        }
        user.getUploads().add(fileDoc);
        user.setUpdatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    public User uploadProfilePhoto(String email, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        String uniqueFilename = "profile_" + email + "_" + UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(uniqueFilename);

        Files.write(filePath, file.getBytes());

        String imageUrl = "/api/users/uploads/" + uniqueFilename;

        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).createdAt(LocalDate.now()).build());
        user.setImageUrl(imageUrl);
        user.setUpdatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    public Path getFilePath(String filename) {
        return uploadDir.resolve(filename);
    }

    public List<FileDocument> getRecentUploads(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        List<User> users = userRepository.findAll();
        List<FileDocument> recentUploads = new ArrayList<>();

        for (User user : users) {
            if (user.getUploads() != null) {
                for (FileDocument upload : user.getUploads()) {
                    if (upload.getUploadedAt() != null && !upload.getUploadedAt().isBefore(cutoffDate)) {
                        recentUploads.add(upload);
                    }
                }
            }
        }
        return recentUploads;
    }

    // New method to display user's enrolled projects
    public List<Project> getEnrolledProjects(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<String> projectIds = user.getEnrolledProjects();
            if (projectIds != null && !projectIds.isEmpty()) {
                return projectRepository.findAllById(projectIds);
            }
        }
        return new ArrayList<>();
    }
    public List<Event> getEnrolledEvents(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<String> eventIds = user.getEnrolledEvents();
            if (eventIds != null && !eventIds.isEmpty()) {
                return eventRepository.findAllById(eventIds);
            }
        }
        return new ArrayList<>();
    }

    
}