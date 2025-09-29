package com.limtic.lab.controller;

import com.limtic.lab.service.FileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/file-types")
@CrossOrigin(origins = "http://localhost:4200")
public class FileTypeController {

    @Autowired
    private FileTypeService fileTypeService;

    @GetMapping
    public List<String> getFileTypes() {
        return fileTypeService.getFileTypes();
    }
}