package com.worklink.hrms.modules.auth.dto;

import com.worklink.hrms.modules.user.entity.User;

public class AuthResponse {

    private String accessToken;
    private String email;
    private User.UserRole role;

    private Long employeeId;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String email, User.UserRole role, Long employeeId) {
        this.accessToken = accessToken;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}