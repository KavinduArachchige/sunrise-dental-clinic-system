package com.sunrise.dentalclinic.config;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    // =========================================================
    // MOCK MVC
    // =========================================================

    @Autowired
    private MockMvc mockMvc;


    // =========================================================
    // TEST USERS
    // =========================================================

    private User adminUser() {

        return new User(
                "admin",
                "password",
                true,
                true,
                true,
                true,
                java.util.List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_ADMIN"
                        )
                )
        );
    }


    private User receptionistUser() {

        return new User(
                "receptionist",
                "password",
                true,
                true,
                true,
                true,
                java.util.List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_RECEPTIONIST"
                        )
                )
        );
    }


    // =========================================================
    // TEST 01
    // LOGIN PAGE MUST BE PUBLIC
    // =========================================================

    @Test
    void shouldAllowPublicAccessToLoginPage()
            throws Exception {

        mockMvc.perform(
                        get("/login")
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 02
    // ACCESS DENIED PAGE MUST BE PUBLIC
    // =========================================================

    @Test
    void shouldAllowAccessDeniedPageWithoutLogin()
            throws Exception {

        mockMvc.perform(
                        get("/access-denied")
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 03
    // UNAUTHENTICATED USER MUST REDIRECT TO LOGIN
    // =========================================================

    @Test
    void shouldRedirectUnauthenticatedUserToLogin()
            throws Exception {

        mockMvc.perform(
                        get("/patients")
                )

                .andExpect(
                        status().is3xxRedirection()
                )

                .andExpect(
                        redirectedUrl(
                                "/login"
                        )
                );
    }


    // =========================================================
    // TEST 04
    // ADMIN CAN ACCESS STAFF
    // =========================================================

    @Test
    void shouldAllowAdminToAccessStaffPage()
            throws Exception {

        mockMvc.perform(
                        get("/staff")
                                .with(
                                        user(
                                                adminUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 05
    // ADMIN CAN ACCESS REPORTS
    // =========================================================

    @Test
    void shouldAllowAdminToAccessReports()
            throws Exception {

        mockMvc.perform(
                        get("/reports")
                                .with(
                                        user(
                                                adminUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 06
    // ADMIN CAN ACCESS AUDIT LOGS
    // =========================================================

    @Test
    void shouldAllowAdminToAccessAuditLogs()
            throws Exception {

        mockMvc.perform(
                        get("/audit-logs")
                                .with(
                                        user(
                                                adminUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 07
    // RECEPTIONIST CANNOT ACCESS STAFF
    // =========================================================

    @Test
    void shouldDenyReceptionistAccessToStaff()
            throws Exception {

        mockMvc.perform(
                        get("/staff")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isForbidden()
                );
    }


    // =========================================================
    // TEST 08
    // RECEPTIONIST CANNOT ACCESS REPORTS
    // =========================================================

    @Test
    void shouldDenyReceptionistAccessToReports()
            throws Exception {

        mockMvc.perform(
                        get("/reports")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isForbidden()
                );
    }


    // =========================================================
    // TEST 09
    // RECEPTIONIST CANNOT ACCESS AUDIT LOGS
    // =========================================================

    @Test
    void shouldDenyReceptionistAccessToAuditLogs()
            throws Exception {

        mockMvc.perform(
                        get("/audit-logs")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isForbidden()
                );
    }


    // =========================================================
    // TEST 10
    // RECEPTIONIST CAN ACCESS PATIENTS
    // =========================================================

    @Test
    void shouldAllowReceptionistToAccessPatients()
            throws Exception {

        mockMvc.perform(
                        get("/patients")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 11
    // RECEPTIONIST CAN ACCESS APPOINTMENTS
    // =========================================================

    @Test
    void shouldAllowReceptionistToAccessAppointments()
            throws Exception {

        mockMvc.perform(
                        get("/appointments")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }


    // =========================================================
    // TEST 12
    // AUTHENTICATED STAFF CAN ACCESS HELP
    // =========================================================

    @Test
    void shouldAllowAuthenticatedStaffToAccessHelp()
            throws Exception {

        mockMvc.perform(
                        get("/help")
                                .with(
                                        user(
                                                receptionistUser()
                                        )
                                )
                )

                .andExpect(
                        status().isOk()
                );
    }
}