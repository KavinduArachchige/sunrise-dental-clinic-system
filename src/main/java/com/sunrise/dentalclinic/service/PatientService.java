package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.repository.PatientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AuditLogService auditLogService;

    public PatientService(
            PatientRepository patientRepository,
            AuditLogService auditLogService
    ) {
        this.patientRepository = patientRepository;
        this.auditLogService = auditLogService;
    }


    // =========================================================
    // REGISTER PATIENT
    // =========================================================

    public Patient registerPatient(Patient patient) {

        if (patientRepository
                .findByContactNumber(patient.getContactNumber())
                .isPresent()) {

            throw new IllegalStateException(
                    "A patient with this contact number already exists."
            );
        }

        Patient savedPatient =
                patientRepository.save(patient);

        auditLogService.log(
                getCurrentUsername(),
                "CREATE_PATIENT",
                "PATIENT",
                "Registered patient: "
                        + savedPatient.getPatientName()
                        + " (ID: "
                        + savedPatient.getPatientId()
                        + ")"
        );

        return savedPatient;
    }


    // =========================================================
    // GET ALL PATIENTS
    // =========================================================

    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }


    // =========================================================
    // GET PATIENT BY ID
    // =========================================================

    public Optional<Patient> getPatientById(Long id) {

        return patientRepository.findById(id);
    }


    // =========================================================
    // GET PATIENT BY CONTACT NUMBER
    // =========================================================

    public Optional<Patient> getPatientByContactNumber(
            String contactNumber
    ) {

        return patientRepository.findByContactNumber(
                contactNumber
        );
    }


    // =========================================================
    // UPDATE PATIENT
    // =========================================================

    public Patient updatePatient(
            Long id,
            Patient updatedPatient
    ) {

        Optional<Patient> existingPatient =
                patientRepository.findById(id);

        if (existingPatient.isPresent()) {

            Patient patient =
                    existingPatient.get();

            String oldName =
                    patient.getPatientName();

            String oldAddress =
                    patient.getAddress();

            String oldContactNumber =
                    patient.getContactNumber();


            patient.setPatientName(
                    updatedPatient.getPatientName()
            );

            patient.setAddress(
                    updatedPatient.getAddress()
            );

            patient.setContactNumber(
                    updatedPatient.getContactNumber()
            );


            Patient savedPatient =
                    patientRepository.save(patient);


            auditLogService.log(
                    getCurrentUsername(),
                    "UPDATE_PATIENT",
                    "PATIENT",
                    "Updated patient ID "
                            + savedPatient.getPatientId()
                            + ". Name: "
                            + oldName
                            + " -> "
                            + savedPatient.getPatientName()
                            + ", Address: "
                            + oldAddress
                            + " -> "
                            + savedPatient.getAddress()
                            + ", Contact: "
                            + oldContactNumber
                            + " -> "
                            + savedPatient.getContactNumber()
            );


            return savedPatient;
        }

        return null;
    }


    // =========================================================
    // DELETE PATIENT
    // =========================================================

    public boolean deletePatient(Long id) {

        Optional<Patient> existingPatient =
                patientRepository.findById(id);

        if (existingPatient.isPresent()) {

            Patient patient =
                    existingPatient.get();

            String patientName =
                    patient.getPatientName();


            patientRepository.deleteById(id);


            auditLogService.log(
                    getCurrentUsername(),
                    "DELETE_PATIENT",
                    "PATIENT",
                    "Deleted patient: "
                            + patientName
                            + " (ID: "
                            + id
                            + ")"
            );


            return true;
        }

        return false;
    }


    // =========================================================
    // CURRENT LOGGED-IN USER
    // =========================================================

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