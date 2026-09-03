package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.StaffResponseDto;
import com.sunrise.dentalclinic.model.StaffUser;
import com.sunrise.dentalclinic.model.UserRole;
import com.sunrise.dentalclinic.repository.StaffUserRepository;
import com.sunrise.dentalclinic.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffManagementController {

    private final StaffUserRepository staffUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public StaffManagementController(
            StaffUserRepository staffUserRepository,
            PasswordEncoder passwordEncoder,
            AuditLogService auditLogService
    ) {
        this.staffUserRepository = staffUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<StaffResponseDto> getAllStaff() {

        return staffUserRepository
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public StaffResponseDto createStaff(
            @RequestBody Map<String, String> request
    ) {

        String username = request.get("username");
        String password = request.get("password");
        String fullName = request.get("fullName");
        String role = request.get("role");

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required."
            );
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters."
            );
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "Full name is required."
            );
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Role is required."
            );
        }

        username = username.trim();

        if (staffUserRepository.existsByUsername(username)) {
            throw new IllegalStateException(
                    "Username already exists."
            );
        }

        UserRole userRole;

        try {
            userRole =
                    UserRole.valueOf(
                            role.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid staff role."
            );
        }

        StaffUser staffUser =
                new StaffUser();

        staffUser.setUsername(
                username
        );

        staffUser.setPassword(
                passwordEncoder.encode(
                        password
                )
        );

        staffUser.setFullName(
                fullName.trim()
        );

        staffUser.setRole(
                userRole
        );

        staffUser.setActive(
                true
        );

        StaffUser savedUser =
                staffUserRepository.save(
                        staffUser
                );

        auditLogService.log(
                getCurrentUsername(),
                "CREATE_STAFF",
                "STAFF",
                "Created staff account: "
                        + savedUser.getFullName()
                        + " (username: "
                        + savedUser.getUsername()
                        + ", role: "
                        + savedUser.getRole()
                        + ")"
        );

        return toDto(savedUser);
    }

    @PutMapping("/{id}/role")
    public StaffResponseDto updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {

        StaffUser staffUser =
                getStaffById(id);

        String requestedRole =
                request.get("role");

        if (
                requestedRole == null ||
                        requestedRole.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Role is required."
            );
        }

        UserRole newRole;

        try {

            newRole =
                    UserRole.valueOf(
                            requestedRole
                                    .trim()
                                    .toUpperCase()
                    );

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid staff role."
            );
        }

        UserRole oldRole =
                staffUser.getRole();

        staffUser.setRole(
                newRole
        );

        StaffUser savedUser =
                staffUserRepository.save(
                        staffUser
                );

        auditLogService.log(
                getCurrentUsername(),
                "CHANGE_STAFF_ROLE",
                "STAFF",
                "Changed role for "
                        + savedUser.getUsername()
                        + " from "
                        + oldRole
                        + " to "
                        + savedUser.getRole()
        );

        return toDto(savedUser);
    }

    @PutMapping("/{id}/status")
    public StaffResponseDto updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request
    ) {

        StaffUser staffUser =
                getStaffById(id);

        Boolean active =
                request.get("active");

        if (active == null) {
            throw new IllegalArgumentException(
                    "Active status is required."
            );
        }

        boolean oldStatus =
                staffUser.isActive();

        staffUser.setActive(
                active
        );

        StaffUser savedUser =
                staffUserRepository.save(
                        staffUser
                );

        String action =
                active
                        ? "ACTIVATE_STAFF"
                        : "DEACTIVATE_STAFF";

        auditLogService.log(
                getCurrentUsername(),
                action,
                "STAFF",
                "Changed account status for "
                        + savedUser.getUsername()
                        + " from "
                        + (oldStatus ? "ACTIVE" : "INACTIVE")
                        + " to "
                        + (savedUser.isActive()
                        ? "ACTIVE"
                        : "INACTIVE")
        );

        return toDto(savedUser);
    }

    @PutMapping("/{id}/password")
    public StaffResponseDto resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {

        StaffUser staffUser =
                getStaffById(id);

        String newPassword =
                request.get("password");

        if (
                newPassword == null ||
                        newPassword.length() < 6
        ) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters."
            );
        }

        staffUser.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        StaffUser savedUser =
                staffUserRepository.save(
                        staffUser
                );

        auditLogService.log(
                getCurrentUsername(),
                "RESET_STAFF_PASSWORD",
                "STAFF",
                "Reset password for staff account: "
                        + savedUser.getUsername()
        );

        return toDto(savedUser);
    }

    private StaffUser getStaffById(Long id) {

        return staffUserRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Staff user not found."
                        )
                );
    }

    private StaffResponseDto toDto(
            StaffUser user
    ) {

        return new StaffResponseDto(
                user.getUserId(),
                user.getFullName(),
                user.getUsername(),
                user.getRole(),
                user.isActive()
        );
    }

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication != null
                        &&
                        authentication.isAuthenticated()
                        &&
                        authentication.getName() != null
                        &&
                        !authentication
                                .getName()
                                .equalsIgnoreCase(
                                        "anonymousUser"
                                )
        ) {
            return authentication.getName();
        }

        return "SYSTEM";
    }
}