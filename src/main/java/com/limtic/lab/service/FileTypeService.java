package com.limtic.lab.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileTypeService {
    private final List<String> fileTypes = new ArrayList<>(Arrays.asList(
        "dataset",
        "certification",
        "research paper",
        "report"
    ));

    public List<String> getFileTypes() {
        return new ArrayList<>(fileTypes); // Return a copy to prevent modification
    }

    public void addFileType(String fileType) {
        if (fileType != null && !fileType.trim().isEmpty() && !fileTypes.contains(fileType.toLowerCase())) {
            fileTypes.add(fileType.toLowerCase());
        }
    }
}