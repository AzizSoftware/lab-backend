package com.limtic.lab.repository;

import com.limtic.lab.model.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<FileDocument, String> {

    // Find files containing a string in title (case-insensitive)
    List<FileDocument> findByTitleContainingIgnoreCase(String title);

    // Find files by keyword (case-insensitive)
    List<FileDocument> findByKeywordsContainingIgnoreCase(String keyword);

    // Find files by author (case-insensitive)
    List<FileDocument> findByAuthorsContainingIgnoreCase(String author);

    // Find files published after a certain date
    List<FileDocument> findByPublicationDateAfter(LocalDate date);

    // Find files published before a certain date
    List<FileDocument> findByPublicationDateBefore(LocalDate date);
}
