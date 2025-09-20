package com.limtic.lab.dto;

import java.time.LocalDate;

public class UserApprovalNotification implements BaseNotification {
    private String type;
    private String email;
    private String event;        // APPROVED or DECLINED
    private String password;
    private LocalDate timestamp;

    public UserApprovalNotification(String type,String email, String event, String paasswprd, LocalDate timestamp) {
        this.type=type;
        this.email = email;
        this.event = event;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

}
