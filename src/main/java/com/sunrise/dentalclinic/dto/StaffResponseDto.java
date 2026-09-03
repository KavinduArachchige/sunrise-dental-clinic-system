package com.sunrise.dentalclinic.dto;

import com.sunrise.dentalclinic.model.UserRole;

public class StaffResponseDto {

    private Long userId;
    private String fullName;
    private String username;
    private UserRole role;
    private boolean active;

    public StaffResponseDto() {
    }

    public StaffResponseDto(
            Long userId,
            String fullName,
            String username,
            UserRole role,
            boolean active
    ) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.role = role;
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}