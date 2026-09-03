package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.AppointmentDetailsDTO;
import com.sunrise.dentalclinic.dto.AppointmentRequestDTO;
import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.AppointmentStatus;
import com.sunrise.dentalclinic.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(
            @RequestBody AppointmentRequestDTO request
    ) {

        Appointment appointment =
                appointmentService.bookAppointment(
                        request.getPatientId(),
                        request.getDentistId(),
                        request.getTreatmentType(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                );

        return ResponseEntity.ok(
                appointment
        );
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id
    ) {

        return appointmentService
                .getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @GetMapping("/number/{appointmentNumber}")
    public ResponseEntity<Appointment> getAppointmentByNumber(
            @PathVariable String appointmentNumber
    ) {

        return appointmentService
                .getAppointmentByNumber(
                        appointmentNumber
                )
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @GetMapping("/details/{appointmentNumber}")
    public ResponseEntity<AppointmentDetailsDTO> getAppointmentDetails(
            @PathVariable String appointmentNumber
    ) {

        AppointmentDetailsDTO details =
                appointmentService
                        .getAppointmentDetailsByNumber(
                                appointmentNumber
                        );

        return ResponseEntity.ok(
                details
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status
    ) {

        return ResponseEntity.ok(
                appointmentService.updateStatus(
                        id,
                        status
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id
    ) {

        boolean deleted =
                appointmentService
                        .deleteAppointment(id);

        if (!deleted) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}