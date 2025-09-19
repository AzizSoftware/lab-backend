package com.limtic.lab.dto;

import java.time.LocalDate;

public class UserApprovalNotification {
    private String email;
    private String event;        // APPROVED or DECLINED
    private String message;
    private LocalDate timestamp;

    public UserApprovalNotification(String email, String event, String message, LocalDate timestamp) {
        this.email = email;
        this.event = event;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

}
