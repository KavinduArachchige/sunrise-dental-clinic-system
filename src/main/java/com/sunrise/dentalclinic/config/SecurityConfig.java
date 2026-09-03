package com.sunrise.dentalclinic.config;

import com.sunrise.dentalclinic.service.CustomUserDetailsService;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService
    ) {
        this.customUserDetailsService =
                customUserDetailsService;
    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );


        provider.setPasswordEncoder(
                passwordEncoder()
        );


        return provider;
    }


    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // -------------------------------------------------
                // CSRF
                // -------------------------------------------------

                .csrf(csrf ->
                        csrf.disable()
                )


                // -------------------------------------------------
                // AUTHENTICATION PROVIDER
                // -------------------------------------------------

                .authenticationProvider(
                        authenticationProvider()
                )


                // -------------------------------------------------
                // AUTHORIZATION
                // -------------------------------------------------

                .authorizeHttpRequests(auth -> auth

                        /*
                         * IMPORTANT FOR JSP
                         *
                         * Spring MVC internally FORWARDS:
                         *
                         * /login
                         *      ↓
                         * /WEB-INF/jsp/login.jsp
                         *
                         * These internal forwards must be allowed.
                         */

                        .dispatcherTypeMatchers(
                                DispatcherType.FORWARD,
                                DispatcherType.ERROR
                        )
                        .permitAll()


                        // -----------------------------------------
                        // PUBLIC URLS
                        // -----------------------------------------

                        .requestMatchers(

                                "/login",

                                "/access-denied",

                                "/css/**",

                                "/js/**",

                                "/images/**",

                                "/favicon.ico",

                                "/error",

                                "/WEB-INF/jsp/**"

                        )
                        .permitAll()


                        // -----------------------------------------
                        // ADMIN ONLY - STAFF MANAGEMENT
                        // -----------------------------------------

                        .requestMatchers(

                                "/staff",
                                "/staff/**",

                                "/api/staff",
                                "/api/staff/**"

                        )
                        .hasRole("ADMIN")


                        // -----------------------------------------
                        // ADMIN ONLY - AUDIT LOGS
                        // -----------------------------------------

                        .requestMatchers(

                                "/audit-logs",
                                "/audit-logs/**",

                                "/api/audit-logs",
                                "/api/audit-logs/**"

                        )
                        .hasRole("ADMIN")


                        // -----------------------------------------
                        // ADMIN ONLY - REPORTS
                        // -----------------------------------------

                        .requestMatchers(

                                "/reports",
                                "/reports/**"

                        )
                        .hasRole("ADMIN")


                        // -----------------------------------------
                        // ALL OTHER SYSTEM PAGES
                        // -----------------------------------------

                        .anyRequest()
                        .authenticated()
                )


                // -------------------------------------------------
                // LOGIN
                // -------------------------------------------------

                .formLogin(form -> form

                        .loginPage(
                                "/login"
                        )

                        .loginProcessingUrl(
                                "/login"
                        )

                        .defaultSuccessUrl(
                                "/",
                                true
                        )

                        .failureUrl(
                                "/login?error=true"
                        )

                        .permitAll()
                )


                // -------------------------------------------------
                // LOGOUT
                // -------------------------------------------------

                .logout(logout -> logout

                        .logoutUrl(
                                "/logout"
                        )

                        .logoutSuccessUrl(
                                "/login?logout=true"
                        )

                        .invalidateHttpSession(
                                true
                        )

                        .deleteCookies(
                                "JSESSIONID"
                        )

                        .permitAll()
                )


                // -------------------------------------------------
                // ACCESS DENIED
                // -------------------------------------------------

                .exceptionHandling(exception -> exception

                        .accessDeniedPage(
                                "/access-denied"
                        )
                );


        return http.build();
    }
}