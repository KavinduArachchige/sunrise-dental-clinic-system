package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.AppointmentDetailsDTO;
import com.sunrise.dentalclinic.dto.AppointmentRequestDTO;
import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.AppointmentStatus;
import com.sunrise.dentalclinic.service.AppointmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppointmentService appointmentService;

    private ObjectMapper objectMapper;

    private AutoCloseable mocks;


    @BeforeEach
    void setUp() {

        mocks =
                MockitoAnnotations.openMocks(this);

        AppointmentController controller =
                new AppointmentController(
                        appointmentService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(controller)
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =========================================================
    // BOOK APPOINTMENT
    // =========================================================

    @Test
    void shouldBookAppointmentSuccessfully()
            throws Exception {

        AppointmentRequestDTO request =
                new AppointmentRequestDTO();

        request.setPatientId(1L);
        request.setDentistId(2L);
        request.setTreatmentType(
                "Dental Cleaning"
        );
        request.setAppointmentDate(
                LocalDate.of(
                        2026,
                        9,
                        10
                )
        );
        request.setAppointmentTime(
                LocalTime.of(
                        10,
                        30
                )
        );


        Appointment appointment =
                new Appointment();

        appointment.setAppointmentId(
                100L
        );

        appointment.setAppointmentNumber(
                "APT-100"
        );

        appointment.setTreatmentType(
                "Dental Cleaning"
        );

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );


        when(
                appointmentService
                        .bookAppointment(
                                eq(1L),
                                eq(2L),
                                eq("Dental Cleaning"),
                                eq(
                                        LocalDate.of(
                                                2026,
                                                9,
                                                10
                                        )
                                ),
                                eq(
                                        LocalTime.of(
                                                10,
                                                30
                                        )
                                )
                        )
        ).thenReturn(
                appointment
        );


        mockMvc.perform(

                        post(
                                "/api/appointments"
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentId"
                        )
                                .value(100)
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentNumber"
                        )
                                .value(
                                        "APT-100"
                                )
                )

                .andExpect(
                        jsonPath(
                                "$.treatmentType"
                        )
                                .value(
                                        "Dental Cleaning"
                                )
                );


        verify(
                appointmentService,
                times(1)
        )
                .bookAppointment(
                        eq(1L),
                        eq(2L),
                        eq("Dental Cleaning"),
                        eq(
                                LocalDate.of(
                                        2026,
                                        9,
                                        10
                                )
                        ),
                        eq(
                                LocalTime.of(
                                        10,
                                        30
                                )
                        )
                );
    }


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================

    @Test
    void shouldReturnAllAppointments()
            throws Exception {

        Appointment appointment1 =
                new Appointment();

        appointment1.setAppointmentId(
                1L
        );

        appointment1.setAppointmentNumber(
                "APT-001"
        );


        Appointment appointment2 =
                new Appointment();

        appointment2.setAppointmentId(
                2L
        );

        appointment2.setAppointmentNumber(
                "APT-002"
        );


        when(
                appointmentService
                        .getAllAppointments()
        ).thenReturn(
                List.of(
                        appointment1,
                        appointment2
                )
        );


        mockMvc.perform(

                        get(
                                "/api/appointments"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.length()"
                        )
                                .value(2)
                )

                .andExpect(
                        jsonPath(
                                "$[0].appointmentNumber"
                        )
                                .value(
                                        "APT-001"
                                )
                )

                .andExpect(
                        jsonPath(
                                "$[1].appointmentNumber"
                        )
                                .value(
                                        "APT-002"
                                )
                );


        verify(
                appointmentService,
                times(1)
        )
                .getAllAppointments();
    }


    // =========================================================
    // GET APPOINTMENT BY ID - SUCCESS
    // =========================================================

    @Test
    void shouldReturnAppointmentById()
            throws Exception {

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentId(
                10L
        );

        appointment.setAppointmentNumber(
                "APT-010"
        );


        when(
                appointmentService
                        .getAppointmentById(
                                10L
                        )
        ).thenReturn(
                Optional.of(
                        appointment
                )
        );


        mockMvc.perform(

                        get(
                                "/api/appointments/10"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentId"
                        )
                                .value(10)
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentNumber"
                        )
                                .value(
                                        "APT-010"
                                )
                );
    }


    // =========================================================
    // GET APPOINTMENT BY ID - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenAppointmentIdDoesNotExist()
            throws Exception {

        when(
                appointmentService
                        .getAppointmentById(
                                999L
                        )
        ).thenReturn(
                Optional.empty()
        );


        mockMvc.perform(

                        get(
                                "/api/appointments/999"
                        )
                )

                .andExpect(
                        status().isNotFound()
                );


        verify(
                appointmentService,
                times(1)
        )
                .getAppointmentById(
                        999L
                );
    }


    // =========================================================
    // GET APPOINTMENT BY NUMBER - SUCCESS
    // =========================================================

    @Test
    void shouldReturnAppointmentByNumber()
            throws Exception {

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentId(
                20L
        );

        appointment.setAppointmentNumber(
                "APT-020"
        );


        when(
                appointmentService
                        .getAppointmentByNumber(
                                "APT-020"
                        )
        ).thenReturn(
                Optional.of(
                        appointment
                )
        );


        mockMvc.perform(

                        get(
                                "/api/appointments/number/APT-020"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentNumber"
                        )
                                .value(
                                        "APT-020"
                                )
                );
    }


    // =========================================================
    // GET APPOINTMENT BY NUMBER - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenAppointmentNumberDoesNotExist()
            throws Exception {

        when(
                appointmentService
                        .getAppointmentByNumber(
                                "APT-999"
                        )
        ).thenReturn(
                Optional.empty()
        );


        mockMvc.perform(

                        get(
                                "/api/appointments/number/APT-999"
                        )
                )

                .andExpect(
                        status().isNotFound()
                );
    }


    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @Test
    void shouldUpdateAppointmentStatus()
            throws Exception {

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentId(
                30L
        );

        appointment.setAppointmentNumber(
                "APT-030"
        );

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );


        when(
                appointmentService
                        .updateStatus(
                                30L,
                                AppointmentStatus.COMPLETED
                        )
        ).thenReturn(
                appointment
        );


        mockMvc.perform(

                        put(
                                "/api/appointments/30/status"
                        )

                                .param(
                                        "status",
                                        "COMPLETED"
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.appointmentId"
                        )
                                .value(30)
                )

                .andExpect(
                        jsonPath(
                                "$.status"
                        )
                                .value(
                                        "COMPLETED"
                                )
                );


        verify(
                appointmentService,
                times(1)
        )
                .updateStatus(
                        30L,
                        AppointmentStatus.COMPLETED
                );
    }


    // =========================================================
    // INVALID STATUS
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidAppointmentStatus()
            throws Exception {

        mockMvc.perform(

                        put(
                                "/api/appointments/30/status"
                        )

                                .param(
                                        "status",
                                        "INVALID_STATUS"
                                )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                appointmentService,
                never()
        )
                .updateStatus(
                        anyLong(),
                        any(
                                AppointmentStatus.class
                        )
                );
    }


    // =========================================================
    // DELETE APPOINTMENT - SUCCESS
    // =========================================================

    @Test
    void shouldDeleteAppointmentSuccessfully()
            throws Exception {

        when(
                appointmentService
                        .deleteAppointment(
                                40L
                        )
        ).thenReturn(
                true
        );


        mockMvc.perform(

                        delete(
                                "/api/appointments/40"
                        )
                )

                .andExpect(
                        status().isNoContent()
                );


        verify(
                appointmentService,
                times(1)
        )
                .deleteAppointment(
                        40L
                );
    }


    // =========================================================
    // DELETE APPOINTMENT - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownAppointment()
            throws Exception {

        when(
                appointmentService
                        .deleteAppointment(
                                999L
                        )
        ).thenReturn(
                false
        );


        mockMvc.perform(

                        delete(
                                "/api/appointments/999"
                        )
                )

                .andExpect(
                        status().isNotFound()
                );


        verify(
                appointmentService,
                times(1)
        )
                .deleteAppointment(
                        999L
                );
    }
}