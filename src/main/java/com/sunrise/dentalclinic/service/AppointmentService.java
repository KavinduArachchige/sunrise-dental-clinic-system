package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.AppointmentDetailsDTO;
import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.AppointmentStatus;
import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Dentist;
import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.DentistRepository;
import com.sunrise.dentalclinic.repository.PatientRepository;
import com.sunrise.dentalclinic.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final AuditLogService auditLogService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DentistRepository dentistRepository,
            BillRepository billRepository,
            PaymentRepository paymentRepository,
            AuditLogService auditLogService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.auditLogService = auditLogService;
    }

    public Appointment bookAppointment(
            Long patientId,
            Long dentistId,
            String treatmentType,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    ) {

        Patient patient =
                patientRepository.findById(patientId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Patient not found with ID: " + patientId
                                )
                        );

        Dentist dentist =
                dentistRepository.findById(dentistId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Dentist not found with ID: " + dentistId
                                )
                        );

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDentistDentistIdAndAppointmentDateAndAppointmentTime(
                                dentistId,
                                appointmentDate,
                                appointmentTime
                        );

        if (alreadyBooked) {
            throw new IllegalStateException(
                    "This dentist already has an appointment at the selected date and time."
            );
        }

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentNumber(
                generateAppointmentNumber()
        );

        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setTreatmentType(treatmentType);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        auditLogService.log(
                getCurrentUsername(),
                "BOOK_APPOINTMENT",
                "APPOINTMENT",
                "Booked appointment "
                        + savedAppointment.getAppointmentNumber()
                        + " for patient "
                        + patient.getPatientName()
                        + " with dentist "
                        + dentist.getDentistName()
                        + " on "
                        + appointmentDate
                        + " at "
                        + appointmentTime
                        + " for treatment "
                        + treatmentType
        );

        return savedAppointment;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Optional<Appointment> getAppointmentByNumber(
            String appointmentNumber
    ) {
        return appointmentRepository
                .findByAppointmentNumber(
                        appointmentNumber
                );
    }

    public AppointmentDetailsDTO getAppointmentDetailsByNumber(
            String appointmentNumber
    ) {

        Appointment appointment =
                appointmentRepository
                        .findByAppointmentNumber(
                                appointmentNumber
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Appointment not found: "
                                                + appointmentNumber
                                )
                        );

        AppointmentDetailsDTO dto =
                new AppointmentDetailsDTO();

        dto.setAppointmentId(
                appointment.getAppointmentId()
        );

        dto.setAppointmentNumber(
                appointment.getAppointmentNumber()
        );

        dto.setTreatmentType(
                appointment.getTreatmentType()
        );

        dto.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        dto.setAppointmentTime(
                appointment.getAppointmentTime()
        );

        dto.setAppointmentStatus(
                appointment.getStatus() != null
                        ? appointment.getStatus().name()
                        : null
        );

        if (appointment.getPatient() != null) {

            dto.setPatientId(
                    appointment.getPatient().getPatientId()
            );

            dto.setPatientName(
                    appointment.getPatient().getPatientName()
            );

            dto.setPatientAddress(
                    appointment.getPatient().getAddress()
            );

            dto.setPatientContactNumber(
                    appointment.getPatient().getContactNumber()
            );
        }

        if (appointment.getDentist() != null) {

            dto.setDentistId(
                    appointment.getDentist().getDentistId()
            );

            dto.setDentistName(
                    appointment.getDentist().getDentistName()
            );

            dto.setDentistSpecialization(
                    appointment.getDentist().getSpecialization()
            );
        }

        Optional<Bill> billOptional =
                billRepository
                        .findByAppointmentAppointmentId(
                                appointment.getAppointmentId()
                        );

        if (billOptional.isPresent()) {

            Bill bill =
                    billOptional.get();

            dto.setBillGenerated(true);

            dto.setBillId(
                    bill.getBillId()
            );

            dto.setBillNumber(
                    bill.getBillNumber()
            );

            dto.setTreatmentAmount(
                    bill.getTreatmentAmount()
            );

            dto.setConsultationFee(
                    bill.getConsultationFee()
            );

            dto.setTotalAmount(
                    bill.getTotalAmount()
            );

            Optional<Payment> paymentOptional =
                    paymentRepository
                            .findByBillBillId(
                                    bill.getBillId()
                            );

            if (paymentOptional.isPresent()) {

                Payment payment =
                        paymentOptional.get();

                dto.setPaymentRecorded(true);

                dto.setReceiptNumber(
                        payment.getReceiptNumber()
                );

                dto.setPaidAmount(
                        payment.getPaidAmount()
                );

                dto.setPaymentMethod(
                        payment.getPaymentMethod()
                );

                dto.setPaymentStatus(
                        payment.getPaymentStatus() != null
                                ? payment.getPaymentStatus().name()
                                : null
                );

            } else {

                dto.setPaymentRecorded(false);
            }

        } else {

            dto.setBillGenerated(false);
            dto.setPaymentRecorded(false);
        }

        return dto;
    }

    public Appointment updateStatus(
            Long id,
            AppointmentStatus status
    ) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Appointment not found with ID: " + id
                                )
                        );

        AppointmentStatus oldStatus =
                appointment.getStatus();

        appointment.setStatus(status);

        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment
                );

        String action =
                status == AppointmentStatus.CANCELLED
                        ? "CANCEL_APPOINTMENT"
                        : "UPDATE_APPOINTMENT_STATUS";

        auditLogService.log(
                getCurrentUsername(),
                action,
                "APPOINTMENT",
                "Appointment "
                        + savedAppointment.getAppointmentNumber()
                        + " status changed from "
                        + oldStatus
                        + " to "
                        + savedAppointment.getStatus()
        );

        return savedAppointment;
    }

    public boolean deleteAppointment(Long id) {

        Optional<Appointment> existingAppointment =
                appointmentRepository.findById(id);

        if (existingAppointment.isPresent()) {

            Appointment appointment =
                    existingAppointment.get();

            String appointmentNumber =
                    appointment.getAppointmentNumber();

            String patientName =
                    appointment.getPatient() != null
                            ? appointment.getPatient().getPatientName()
                            : "Unknown Patient";

            appointmentRepository.deleteById(id);

            auditLogService.log(
                    getCurrentUsername(),
                    "DELETE_APPOINTMENT",
                    "APPOINTMENT",
                    "Deleted appointment "
                            + appointmentNumber
                            + " for patient "
                            + patientName
                            + " (ID: "
                            + id
                            + ")"
            );

            return true;
        }

        return false;
    }

    private String generateAppointmentNumber() {

        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        return "APT-" + randomPart;
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