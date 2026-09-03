package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.StaffUser;
import com.sunrise.dentalclinic.repository.StaffUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffUserRepository staffUserRepository;

    public CustomUserDetailsService(StaffUserRepository staffUserRepository) {
        this.staffUserRepository = staffUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        StaffUser staffUser = staffUserRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        if (!staffUser.isActive()) {
            throw new UsernameNotFoundException(
                    "User account is inactive."
            );
        }

        return User.builder()
                .username(staffUser.getUsername())
                .password(staffUser.getPassword())
                .roles(staffUser.getRole().name())
                .build();
    }
}