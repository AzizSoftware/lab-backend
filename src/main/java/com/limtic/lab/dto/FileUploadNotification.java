package com.limtic.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadNotification implements BaseNotification {
    private String type;
    private String userEmail;
    private String title;
    private LocalDate publicationDate;
    private String message;
}
