package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.repository.PatientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {

        patient = new Patient();

        patient.setPatientId(1L);
        patient.setPatientName("Nimal Perera");
        patient.setAddress("Colombo");
        patient.setContactNumber("0771234567");
    }


    // =========================================================
    // TEST 01 - REGISTER PATIENT SUCCESS
    // =========================================================

    @Test
    void shouldRegisterPatientSuccessfully() {

        when(
                patientRepository.findByContactNumber(
                        "0771234567"
                )
        ).thenReturn(
                Optional.empty()
        );

        when(
                patientRepository.save(any(Patient.class))
        ).thenReturn(
                patient
        );


        Patient result =
                patientService.registerPatient(
                        patient
                );


        assertNotNull(result);

        assertEquals(
                "Nimal Perera",
                result.getPatientName()
        );

        assertEquals(
                "0771234567",
                result.getContactNumber()
        );


        verify(
                patientRepository,
                times(1)
        ).save(patient);


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("CREATE_PATIENT"),
                eq("PATIENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 02 - DUPLICATE CONTACT NUMBER
    // =========================================================

    @Test
    void shouldRejectDuplicateContactNumber() {

        when(
                patientRepository.findByContactNumber(
                        "0771234567"
                )
        ).thenReturn(
                Optional.of(patient)
        );


        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                patientService
                                        .registerPatient(
                                                patient
                                        )
                );


        assertTrue(
                exception
                        .getMessage()
                        .contains(
                                "already exists"
                        )
        );


        verify(
                patientRepository,
                never()
        ).save(any(Patient.class));
    }


    // =========================================================
    // TEST 03 - GET ALL PATIENTS
    // =========================================================

    @Test
    void shouldReturnAllPatients() {

        Patient secondPatient =
                new Patient();

        secondPatient.setPatientId(2L);
        secondPatient.setPatientName("Sarath Perera");
        secondPatient.setAddress("Homagama");
        secondPatient.setContactNumber("0712345678");


        when(
                patientRepository.findAll()
        ).thenReturn(
                List.of(
                        patient,
                        secondPatient
                )
        );


        List<Patient> result =
                patientService.getAllPatients();


        assertEquals(
                2,
                result.size()
        );


        verify(
                patientRepository,
                times(1)
        ).findAll();
    }


    // =========================================================
    // TEST 04 - FIND PATIENT BY ID
    // =========================================================

    @Test
    void shouldFindPatientById() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        Optional<Patient> result =
                patientService.getPatientById(
                        1L
                );


        assertTrue(
                result.isPresent()
        );

        assertEquals(
                "Nimal Perera",
                result.get().getPatientName()
        );
    }


    // =========================================================
    // TEST 05 - UPDATE PATIENT
    // =========================================================

    @Test
    void shouldUpdatePatientSuccessfully() {

        Patient updatedPatient =
                new Patient();

        updatedPatient.setPatientName(
                "Nimal Silva"
        );

        updatedPatient.setAddress(
                "Kandy"
        );

        updatedPatient.setContactNumber(
                "0779999999"
        );


        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );

        when(
                patientRepository.save(any(Patient.class))
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Patient result =
                patientService.updatePatient(
                        1L,
                        updatedPatient
                );


        assertNotNull(result);

        assertEquals(
                "Nimal Silva",
                result.getPatientName()
        );

        assertEquals(
                "Kandy",
                result.getAddress()
        );

        assertEquals(
                "0779999999",
                result.getContactNumber()
        );


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("UPDATE_PATIENT"),
                eq("PATIENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 06 - UPDATE NON-EXISTING PATIENT
    // =========================================================

    @Test
    void shouldReturnNullWhenUpdatingUnknownPatient() {

        when(
                patientRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        Patient result =
                patientService.updatePatient(
                        99L,
                        patient
                );


        assertNull(result);


        verify(
                patientRepository,
                never()
        ).save(any(Patient.class));
    }


    // =========================================================
    // TEST 07 - DELETE PATIENT
    // =========================================================

    @Test
    void shouldDeletePatientSuccessfully() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        boolean result =
                patientService.deletePatient(
                        1L
                );


        assertTrue(result);


        verify(
                patientRepository,
                times(1)
        ).deleteById(1L);


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("DELETE_PATIENT"),
                eq("PATIENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 08 - DELETE UNKNOWN PATIENT
    // =========================================================

    @Test
    void shouldReturnFalseWhenDeletingUnknownPatient() {

        when(
                patientRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        boolean result =
                patientService.deletePatient(
                        99L
                );


        assertFalse(result);


        verify(
                patientRepository,
                never()
        ).deleteById(anyLong());
    }
}