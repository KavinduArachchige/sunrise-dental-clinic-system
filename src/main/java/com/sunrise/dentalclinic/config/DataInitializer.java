package com.sunrise.dentalclinic.config;

import com.sunrise.dentalclinic.model.StaffUser;
import com.sunrise.dentalclinic.model.UserRole;
import com.sunrise.dentalclinic.repository.StaffUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StaffUserRepository staffUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            StaffUserRepository staffUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.staffUserRepository = staffUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        createUserIfNotExists(
                "admin",
                "Admin@123",
                "System Administrator",
                UserRole.ADMIN
        );

        createUserIfNotExists(
                "receptionist",
                "Reception@123",
                "Clinic Receptionist",
                UserRole.RECEPTIONIST
        );
    }

    private void createUserIfNotExists(
            String username,
            String rawPassword,
            String fullName,
            UserRole role
    ) {

        if (staffUserRepository.findByUsername(username).isEmpty()) {

            StaffUser user = new StaffUser();

            user.setUsername(username);

            user.setPassword(
                    passwordEncoder.encode(rawPassword)
            );

            user.setFullName(fullName);

            user.setRole(role);

            user.setActive(true);

            staffUserRepository.save(user);
        }
    }
}