package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.AppointmentStatus;
import com.sunrise.dentalclinic.model.Dentist;
import com.sunrise.dentalclinic.model.Patient;

import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.DentistRepository;
import com.sunrise.dentalclinic.repository.PatientRepository;
import com.sunrise.dentalclinic.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DentistRepository dentistRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AppointmentService appointmentService;


    private Patient patient;
    private Dentist dentist;
    private Appointment appointment;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;


    @BeforeEach
    void setUp() {

        patient = new Patient();

        patient.setPatientId(1L);
        patient.setPatientName("Nimal Perera");
        patient.setAddress("Homagama");
        patient.setContactNumber("0771234567");


        dentist = new Dentist();

        dentist.setDentistId(1L);
        dentist.setDentistName("Dr. Kasun Silva");
        dentist.setSpecialization("General Dentistry");


        appointmentDate =
                LocalDate.of(
                        2026,
                        9,
                        10
                );

        appointmentTime =
                LocalTime.of(
                        10,
                        30
                );


        appointment = new Appointment();

        appointment.setAppointmentId(1L);
        appointment.setAppointmentNumber("APT-ABC12345");

        appointment.setPatient(patient);
        appointment.setDentist(dentist);

        appointment.setTreatmentType(
                "Dental Cleaning"
        );

        appointment.setAppointmentDate(
                appointmentDate
        );

        appointment.setAppointmentTime(
                appointmentTime
        );

        appointment.setStatus(
                AppointmentStatus.SCHEDULED
        );
    }


    // =========================================================
    // TEST 01
    // SUCCESSFUL APPOINTMENT BOOKING
    // =========================================================

    @Test
    void shouldBookAppointmentSuccessfully() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        when(
                dentistRepository.findById(1L)
        ).thenReturn(
                Optional.of(dentist)
        );


        when(
                appointmentRepository
                        .existsByDentistDentistIdAndAppointmentDateAndAppointmentTime(
                                1L,
                                appointmentDate,
                                appointmentTime
                        )
        ).thenReturn(false);


        when(
                appointmentRepository.save(
                        any(Appointment.class)
                )
        ).thenAnswer(invocation -> {

            Appointment saved =
                    invocation.getArgument(0);

            saved.setAppointmentId(1L);

            return saved;
        });


        Appointment result =
                appointmentService.bookAppointment(
                        1L,
                        1L,
                        "Dental Cleaning",
                        appointmentDate,
                        appointmentTime
                );


        assertNotNull(result);

        assertEquals(
                patient,
                result.getPatient()
        );

        assertEquals(
                dentist,
                result.getDentist()
        );

        assertEquals(
                "Dental Cleaning",
                result.getTreatmentType()
        );

        assertEquals(
                AppointmentStatus.SCHEDULED,
                result.getStatus()
        );


        verify(
                appointmentRepository,
                times(1)
        ).save(any(Appointment.class));


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("BOOK_APPOINTMENT"),
                eq("APPOINTMENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 02
    // UNIQUE APPOINTMENT NUMBER FORMAT
    // =========================================================

    @Test
    void shouldGenerateAppointmentNumberWhenBooking() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        when(
                dentistRepository.findById(1L)
        ).thenReturn(
                Optional.of(dentist)
        );


        when(
                appointmentRepository
                        .existsByDentistDentistIdAndAppointmentDateAndAppointmentTime(
                                1L,
                                appointmentDate,
                                appointmentTime
                        )
        ).thenReturn(false);


        when(
                appointmentRepository.save(
                        any(Appointment.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Appointment result =
                appointmentService.bookAppointment(
                        1L,
                        1L,
                        "Dental Cleaning",
                        appointmentDate,
                        appointmentTime
                );


        assertNotNull(
                result.getAppointmentNumber()
        );


        assertTrue(
                result
                        .getAppointmentNumber()
                        .startsWith("APT-")
        );


        assertEquals(
                12,
                result
                        .getAppointmentNumber()
                        .length()
        );
    }


    // =========================================================
    // TEST 03
    // PATIENT NOT FOUND
    // =========================================================

    @Test
    void shouldRejectBookingWhenPatientDoesNotExist() {

        when(
                patientRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                appointmentService
                                        .bookAppointment(
                                                99L,
                                                1L,
                                                "Dental Cleaning",
                                                appointmentDate,
                                                appointmentTime
                                        )
                );


        assertTrue(
                exception
                        .getMessage()
                        .contains("Patient not found")
        );


        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));
    }


    // =========================================================
    // TEST 04
    // DENTIST NOT FOUND
    // =========================================================

    @Test
    void shouldRejectBookingWhenDentistDoesNotExist() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        when(
                dentistRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                appointmentService
                                        .bookAppointment(
                                                1L,
                                                99L,
                                                "Dental Cleaning",
                                                appointmentDate,
                                                appointmentTime
                                        )
                );


        assertTrue(
                exception
                        .getMessage()
                        .contains("Dentist not found")
        );


        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));
    }


    // =========================================================
    // TEST 05
    // DOUBLE BOOKING PROTECTION
    // =========================================================

    @Test
    void shouldRejectDoubleBookingForSameDentistDateAndTime() {

        when(
                patientRepository.findById(1L)
        ).thenReturn(
                Optional.of(patient)
        );


        when(
                dentistRepository.findById(1L)
        ).thenReturn(
                Optional.of(dentist)
        );


        when(
                appointmentRepository
                        .existsByDentistDentistIdAndAppointmentDateAndAppointmentTime(
                                1L,
                                appointmentDate,
                                appointmentTime
                        )
        ).thenReturn(true);


        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                appointmentService
                                        .bookAppointment(
                                                1L,
                                                1L,
                                                "Dental Cleaning",
                                                appointmentDate,
                                                appointmentTime
                                        )
                );


        assertTrue(
                exception
                        .getMessage()
                        .contains(
                                "already has an appointment"
                        )
        );


        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));


        verify(
                auditLogService,
                never()
        ).log(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        );
    }


    // =========================================================
    // TEST 06
    // GET ALL APPOINTMENTS
    // =========================================================

    @Test
    void shouldReturnAllAppointments() {

        Appointment secondAppointment =
                new Appointment();

        secondAppointment.setAppointmentId(2L);

        secondAppointment.setAppointmentNumber(
                "APT-XYZ12345"
        );


        when(
                appointmentRepository.findAll()
        ).thenReturn(
                List.of(
                        appointment,
                        secondAppointment
                )
        );


        List<Appointment> result =
                appointmentService
                        .getAllAppointments();


        assertEquals(
                2,
                result.size()
        );


        verify(
                appointmentRepository,
                times(1)
        ).findAll();
    }


    // =========================================================
    // TEST 07
    // SEARCH BY APPOINTMENT NUMBER
    // =========================================================

    @Test
    void shouldFindAppointmentByNumber() {

        when(
                appointmentRepository
                        .findByAppointmentNumber(
                                "APT-ABC12345"
                        )
        ).thenReturn(
                Optional.of(appointment)
        );


        Optional<Appointment> result =
                appointmentService
                        .getAppointmentByNumber(
                                "APT-ABC12345"
                        );


        assertTrue(
                result.isPresent()
        );


        assertEquals(
                "APT-ABC12345",
                result
                        .get()
                        .getAppointmentNumber()
        );
    }


    // =========================================================
    // TEST 08
    // UPDATE STATUS
    // =========================================================

    @Test
    void shouldUpdateAppointmentStatusSuccessfully() {

        when(
                appointmentRepository.findById(1L)
        ).thenReturn(
                Optional.of(appointment)
        );


        when(
                appointmentRepository.save(
                        any(Appointment.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Appointment result =
                appointmentService.updateStatus(
                        1L,
                        AppointmentStatus.COMPLETED
                );


        assertEquals(
                AppointmentStatus.COMPLETED,
                result.getStatus()
        );


        verify(
                appointmentRepository,
                times(1)
        ).save(appointment);


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("UPDATE_APPOINTMENT_STATUS"),
                eq("APPOINTMENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 09
    // CANCEL APPOINTMENT
    // =========================================================

    @Test
    void shouldLogCancellationWhenAppointmentIsCancelled() {

        when(
                appointmentRepository.findById(1L)
        ).thenReturn(
                Optional.of(appointment)
        );


        when(
                appointmentRepository.save(
                        any(Appointment.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Appointment result =
                appointmentService.updateStatus(
                        1L,
                        AppointmentStatus.CANCELLED
                );


        assertEquals(
                AppointmentStatus.CANCELLED,
                result.getStatus()
        );


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("CANCEL_APPOINTMENT"),
                eq("APPOINTMENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 10
    // UNKNOWN APPOINTMENT STATUS UPDATE
    // =========================================================

    @Test
    void shouldRejectStatusUpdateForUnknownAppointment() {

        when(
                appointmentRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                appointmentService
                                        .updateStatus(
                                                99L,
                                                AppointmentStatus.COMPLETED
                                        )
                );


        assertTrue(
                exception
                        .getMessage()
                        .contains(
                                "Appointment not found"
                        )
        );


        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));
    }


    // =========================================================
    // TEST 11
    // DELETE APPOINTMENT
    // =========================================================

    @Test
    void shouldDeleteAppointmentSuccessfully() {

        when(
                appointmentRepository.findById(1L)
        ).thenReturn(
                Optional.of(appointment)
        );


        boolean result =
                appointmentService
                        .deleteAppointment(1L);


        assertTrue(result);


        verify(
                appointmentRepository,
                times(1)
        ).deleteById(1L);


        verify(
                auditLogService,
                times(1)
        ).log(
                anyString(),
                eq("DELETE_APPOINTMENT"),
                eq("APPOINTMENT"),
                anyString()
        );
    }


    // =========================================================
    // TEST 12
    // DELETE UNKNOWN APPOINTMENT
    // =========================================================

    @Test
    void shouldReturnFalseWhenDeletingUnknownAppointment() {

        when(
                appointmentRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        boolean result =
                appointmentService
                        .deleteAppointment(99L);


        assertFalse(result);


        verify(
                appointmentRepository,
                never()
        ).deleteById(anyLong());


        verify(
                auditLogService,
                never()
        ).log(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        );
    }
}