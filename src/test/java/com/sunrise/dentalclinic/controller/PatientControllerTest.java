package com.sunrise.dentalclinic.controller;

import tools.jackson.databind.ObjectMapper;
import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PatientControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private PatientService patientService;


    // =========================================================
    // SETUP
    // =========================================================

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        PatientController patientController =
                new PatientController(
                        patientService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(
                                patientController
                        )
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =========================================================
    // REGISTER PATIENT - SUCCESS
    // =========================================================

    @Test
    void shouldRegisterPatientSuccessfully()
            throws Exception {

        Patient request =
                createPatient(
                        1L,
                        "Nimal Perera",
                        "0771234567"
                );

        Patient savedPatient =
                createPatient(
                        1L,
                        "Nimal Perera",
                        "0771234567"
                );

        when(
                patientService.registerPatient(
                        any(Patient.class)
                )
        )
                .thenReturn(
                        savedPatient
                );


        mockMvc.perform(

                        post("/api/patients")

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
                        jsonPath("$.patientId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.patientName")
                                .value("Nimal Perera")
                )

                .andExpect(
                        jsonPath("$.contactNumber")
                                .value("0771234567")
                );


        verify(
                patientService
        )
                .registerPatient(
                        any(Patient.class)
                );
    }


    // =========================================================
    // REGISTER PATIENT - DUPLICATE / SERVICE REJECTION
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenRegistrationRejected()
            throws Exception {

        Patient request =
                createPatient(
                        null,
                        "Nimal Perera",
                        "0771234567"
                );

        when(
                patientService.registerPatient(
                        any(Patient.class)
                )
        )
                .thenThrow(
                        new IllegalStateException(
                                "Patient already exists."
                        )
                );


        mockMvc.perform(

                        post("/api/patients")

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
                        status().isBadRequest()
                )

                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Patient already exists."
                                )
                );
    }


    // =========================================================
    // GET ALL PATIENTS
    // =========================================================

    @Test
    void shouldReturnAllPatients()
            throws Exception {

        Patient patientOne =
                createPatient(
                        1L,
                        "Nimal Perera",
                        "0771234567"
                );

        Patient patientTwo =
                createPatient(
                        2L,
                        "Kamal Silva",
                        "0719876543"
                );


        when(
                patientService.getAllPatients()
        )
                .thenReturn(
                        List.of(
                                patientOne,
                                patientTwo
                        )
                );


        mockMvc.perform(

                        get("/api/patients")
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$[0].patientId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$[0].patientName")
                                .value("Nimal Perera")
                )

                .andExpect(
                        jsonPath("$[1].patientId")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$[1].patientName")
                                .value("Kamal Silva")
                );


        verify(
                patientService
        )
                .getAllPatients();
    }


    // =========================================================
    // GET ALL PATIENTS - EMPTY LIST
    // =========================================================

    @Test
    void shouldReturnEmptyPatientList()
            throws Exception {

        when(
                patientService.getAllPatients()
        )
                .thenReturn(
                        List.of()
                );


        mockMvc.perform(

                        get("/api/patients")
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        content().json("[]")
                );
    }


    // =========================================================
    // GET PATIENT BY ID - FOUND
    // =========================================================

    @Test
    void shouldReturnPatientByIdWhenFound()
            throws Exception {

        Patient patient =
                createPatient(
                        1L,
                        "Nimal Perera",
                        "0771234567"
                );


        when(
                patientService.getPatientById(
                        1L
                )
        )
                .thenReturn(
                        Optional.of(
                                patient
                        )
                );


        mockMvc.perform(

                        get("/api/patients/{id}", 1L)
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.patientId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.patientName")
                                .value("Nimal Perera")
                )

                .andExpect(
                        jsonPath("$.contactNumber")
                                .value("0771234567")
                );


        verify(
                patientService
        )
                .getPatientById(
                        1L
                );
    }


    // =========================================================
    // GET PATIENT BY ID - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenPatientIdDoesNotExist()
            throws Exception {

        when(
                patientService.getPatientById(
                        99L
                )
        )
                .thenReturn(
                        Optional.empty()
                );


        mockMvc.perform(

                        get("/api/patients/{id}", 99L)
                )

                .andExpect(
                        status().isNotFound()
                );
    }


    // =========================================================
    // GET PATIENT BY CONTACT - FOUND
    // =========================================================

    @Test
    void shouldReturnPatientByContactNumberWhenFound()
            throws Exception {

        Patient patient =
                createPatient(
                        1L,
                        "Nimal Perera",
                        "0771234567"
                );


        when(
                patientService
                        .getPatientByContactNumber(
                                "0771234567"
                        )
        )
                .thenReturn(
                        Optional.of(
                                patient
                        )
                );


        mockMvc.perform(

                        get(
                                "/api/patients/contact/{contactNumber}",
                                "0771234567"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.patientId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.contactNumber")
                                .value("0771234567")
                );
    }


    // =========================================================
    // GET PATIENT BY CONTACT - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenContactNumberDoesNotExist()
            throws Exception {

        when(
                patientService
                        .getPatientByContactNumber(
                                "0000000000"
                        )
        )
                .thenReturn(
                        Optional.empty()
                );


        mockMvc.perform(

                        get(
                                "/api/patients/contact/{contactNumber}",
                                "0000000000"
                        )
                )

                .andExpect(
                        status().isNotFound()
                );
    }


    // =========================================================
    // UPDATE PATIENT - SUCCESS
    // =========================================================

    @Test
    void shouldUpdatePatientSuccessfully()
            throws Exception {

        Patient request =
                createPatient(
                        null,
                        "Nimal Perera Updated",
                        "0779999999"
                );

        Patient updatedPatient =
                createPatient(
                        1L,
                        "Nimal Perera Updated",
                        "0779999999"
                );


        when(
                patientService.updatePatient(
                        eq(1L),
                        any(Patient.class)
                )
        )
                .thenReturn(
                        updatedPatient
                );


        mockMvc.perform(

                        put("/api/patients/{id}", 1L)

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
                        jsonPath("$.patientId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.patientName")
                                .value(
                                        "Nimal Perera Updated"
                                )
                )

                .andExpect(
                        jsonPath("$.contactNumber")
                                .value(
                                        "0779999999"
                                )
                );


        verify(
                patientService
        )
                .updatePatient(
                        eq(1L),
                        any(Patient.class)
                );
    }


    // =========================================================
    // UPDATE PATIENT - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenUpdatingUnknownPatient()
            throws Exception {

        Patient request =
                createPatient(
                        null,
                        "Unknown Patient",
                        "0770000000"
                );


        when(
                patientService.updatePatient(
                        eq(99L),
                        any(Patient.class)
                )
        )
                .thenReturn(
                        null
                );


        mockMvc.perform(

                        put("/api/patients/{id}", 99L)

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
                        status().isNotFound()
                );
    }


    // =========================================================
    // DELETE PATIENT - SUCCESS
    // =========================================================

    @Test
    void shouldDeletePatientSuccessfully()
            throws Exception {

        when(
                patientService.deletePatient(
                        1L
                )
        )
                .thenReturn(
                        true
                );


        mockMvc.perform(

                        delete("/api/patients/{id}", 1L)
                )

                .andExpect(
                        status().isNoContent()
                )

                .andExpect(
                        content().string("")
                );


        verify(
                patientService
        )
                .deletePatient(
                        1L
                );
    }


    // =========================================================
    // DELETE PATIENT - NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownPatient()
            throws Exception {

        when(
                patientService.deletePatient(
                        99L
                )
        )
                .thenReturn(
                        false
                );


        mockMvc.perform(

                        delete("/api/patients/{id}", 99L)
                )

                .andExpect(
                        status().isNotFound()
                );


        verify(
                patientService
        )
                .deletePatient(
                        99L
                );
    }


    // =========================================================
    // INVALID PATIENT ID FORMAT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidPatientId()
            throws Exception {

        mockMvc.perform(

                        get("/api/patients/not-a-number")
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                patientService,
                never()
        )
                .getPatientById(
                        any()
                );
    }


    // =========================================================
    // HELPER METHOD
    // =========================================================

    private Patient createPatient(
            Long patientId,
            String patientName,
            String contactNumber
    ) {

        Patient patient =
                new Patient();

        patient.setPatientId(
                patientId
        );

        patient.setPatientName(
                patientName
        );

        patient.setContactNumber(
                contactNumber
        );

        return patient;
    }
}