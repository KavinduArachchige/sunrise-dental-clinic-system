package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Dentist;
import com.sunrise.dentalclinic.service.DentistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentists")
public class DentistController {

    private final DentistService dentistService;

    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    // Add a new dentist
    @PostMapping
    public ResponseEntity<Dentist> addDentist(@RequestBody Dentist dentist) {
        Dentist savedDentist = dentistService.addDentist(dentist);
        return ResponseEntity.ok(savedDentist);
    }

    // Get all dentists
    @GetMapping
    public ResponseEntity<List<Dentist>> getAllDentists() {
        return ResponseEntity.ok(dentistService.getAllDentists());
    }

    // Get dentist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Dentist> getDentistById(@PathVariable Long id) {
        return dentistService.getDentistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get dentist by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Dentist> getDentistByEmail(@PathVariable String email) {
        return dentistService.getDentistByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update dentist
    @PutMapping("/{id}")
    public ResponseEntity<Dentist> updateDentist(
            @PathVariable Long id,
            @RequestBody Dentist dentist) {

        Dentist updatedDentist = dentistService.updateDentist(id, dentist);

        if (updatedDentist == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedDentist);
    }

    // Delete dentist
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable Long id) {

        boolean deleted = dentistService.deleteDentist(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}