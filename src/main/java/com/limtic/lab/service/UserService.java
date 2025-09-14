package com.limtic.lab.service;

import com.limtic.lab.model.FileDocument;
import com.limtic.lab.model.User;
import com.limtic.lab.repository.FileRepository;
import com.limtic.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    private final Path uploadDir = Paths.get("uploads");

     public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory", e);
        }
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
        String doi
    ) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        String uniqueFilename = UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(uniqueFilename);
        Files.write(filePath, file.getBytes());

        User user = userRepository.findByEmail(email)
                .orElse(User.builder().email(email).createdAt(LocalDate.now()).build());

        FileDocument fileDoc = new FileDocument();
        fileDoc.setTitle(title != null ? title : originalFilename);
        fileDoc.setAuthors(authors);
        fileDoc.setAffiliations(affiliations);
        fileDoc.setPublicationDate(publicationDate);
        fileDoc.setAbstractText(abstractText);
        fileDoc.setKeywords(keywords);
        fileDoc.setDoi(doi != null ? doi : UUID.randomUUID().toString());
        fileDoc.setOwnerId(user.getId());
        fileDoc.setUploadedAt(LocalDate.now());

        // 1️⃣ Save in files collection
        fileRepository.save(fileDoc);

        // 2️⃣ Add to user's uploads
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
}
