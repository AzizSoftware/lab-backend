package com.limtic.lab.service;

import com.limtic.lab.model.FileDocument;
import com.limtic.lab.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public List<FileDocument> getAllFiles() {
        return fileRepository.findAll();
    }

    public long countFiles() {
        return fileRepository.count();
    }

    public Optional<FileDocument> getFileById(String id) {
        return fileRepository.findById(id);
    }

    public List<FileDocument> findByTitle(String title) {
        return fileRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<FileDocument> findByKeyword(String keyword) {
        return fileRepository.findByKeywordsContainingIgnoreCase(keyword);
    }

    public List<FileDocument> findByAuthor(String author) {
        return fileRepository.findByAuthorsContainingIgnoreCase(author);
    }

    public List<FileDocument> findByDateAfter(LocalDate date) {
        return fileRepository.findByPublicationDateAfter(date);
    }

    public List<FileDocument> findByDateBefore(LocalDate date) {
        return fileRepository.findByPublicationDateBefore(date);
    }

    public FileDocument saveFile(FileDocument fileDocument) {
        return fileRepository.save(fileDocument);
    }

    public FileDocument updateFile(String id, FileDocument updatedFile) {
        return fileRepository.findById(id).map(file -> {
            file.setFilename(updatedFile.getFilename());
            file.setFileType(updatedFile.getFileType());
            file.setTitle(updatedFile.getTitle());
            file.setAuthors(updatedFile.getAuthors());
            file.setAffiliations(updatedFile.getAffiliations());
            file.setPublicationDate(updatedFile.getPublicationDate());
            file.setAbstractText(updatedFile.getAbstractText());
            file.setKeywords(updatedFile.getKeywords());
            file.setDoi(updatedFile.getDoi());
            return fileRepository.save(file);
        }).orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }

    public void deleteFile(String id) {
        fileRepository.deleteById(id);
    }
}
