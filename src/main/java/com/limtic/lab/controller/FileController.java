package com.limtic.lab.controller;

import com.limtic.lab.model.FileDocument;
import com.limtic.lab.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public List<FileDocument> getAllFiles() {
        return fileService.getAllFiles();
    }

    // Get file by ID
    @GetMapping("/{id}")
    public Optional<FileDocument> getFileById(@PathVariable String id) {
        return fileService.getFileById(id);
    }

    // Create new file (without upload, just metadata)
    @PostMapping
    public FileDocument createFile(@RequestBody FileDocument fileDocument) {
        return fileService.saveFile(fileDocument);
    }

    // Update file
    @PutMapping("/{id}")
    public FileDocument updateFile(@PathVariable String id, @RequestBody FileDocument updatedFile) {
        return fileService.updateFile(id, updatedFile);
    }

    // Delete file
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable String id) {
        fileService.deleteFile(id);
    }

    // Count all files
    @GetMapping("/count")
    public long countFiles() {
        return fileService.countFiles();
    }

    // Search by title
    @GetMapping("/search/title")
    public List<FileDocument> searchByTitle(@RequestParam String title) {
        return fileService.findByTitle(title);
    }

    // Search by keyword
    @GetMapping("/search/keyword")
    public List<FileDocument> searchByKeyword(@RequestParam String keyword) {
        return fileService.findByKeyword(keyword);
    }

    // Search by author
    @GetMapping("/search/author")
    public List<FileDocument> searchByAuthor(@RequestParam String author) {
        return fileService.findByAuthor(author);
    }

    // Search by date after
    @GetMapping("/search/dateAfter")
    public List<FileDocument> searchByDateAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return fileService.findByDateAfter(date);
    }

    // Search by date before
    @GetMapping("/search/dateBefore")
    public List<FileDocument> searchByDateBefore(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return fileService.findByDateBefore(date);
    }

    //search by ranking
    @GetMapping("/search/ranking")
    public List<FileDocument> searchByRank(@RequestParam String rank) {
        return fileService.findByRank(rank);
    }

    @GetMapping("search/type")
    public List<FileDocument> searchByType(@RequestParam String type) {
        return fileService.findByType(type);
    }
    
    
}

