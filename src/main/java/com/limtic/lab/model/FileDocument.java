package com.limtic.lab.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "files")
public class FileDocument {

    @Id
    private String id;

    private String filename; // Stores the unique filename (e.g., UUID_originalfilename)
    private String fileType; // Stores the file type (e.g., dataset, certification, research paper, report, or custom)
    private String title;
    private List<String> authors;
    private List<String> affiliations;
    private LocalDate publicationDate;
    private String abstractText;
    private List<String> keywords;
    private String doi;
    private String ownerId; // Reference to User.id
    private LocalDate uploadedAt;
}