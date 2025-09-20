package com.limtic.lab.dto;

import java.time.LocalDate;

public class UserSignupNotification implements BaseNotification{
    private String type;
    private String email;
    private String event;
    private String message;
    private LocalDate createdAt;

    // Constructor
    public UserSignupNotification(String type,String email, String event, LocalDate createdAt) {
        this.type=type;
        this.email = email;
        this.event = event;
        this.createdAt = createdAt;
        this.message = buildMessage();
    }

    private String buildMessage() {
        if ("NEW_USER_SIGNUP".equals(event)) {
            return String.format(" New user signed up: %s. Please verify.", email);
        } else if ("NEW_ADMIN_CREATED".equals(event)) {
            return String.format(" New admin created: %s.", email);
        }
        return "Unknown event for " + email;
    }

    // Getters
    public String getEmail() { return email; }
    public String getEvent() { return event; }
    public String getMessage() { return message; }
    public LocalDate getCreatedAt() { return createdAt; }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }
}
