package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Treatment;
import com.sunrise.dentalclinic.service.TreatmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping
    public ResponseEntity<Treatment> addTreatment(
            @RequestBody Treatment treatment) {

        return ResponseEntity.ok(
                treatmentService.addTreatment(treatment)
        );
    }

    @GetMapping
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        return ResponseEntity.ok(
                treatmentService.getAllTreatments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getTreatmentById(
            @PathVariable Long id) {

        return treatmentService.getTreatmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treatment> updateTreatment(
            @PathVariable Long id,
            @RequestBody Treatment treatment) {

        return ResponseEntity.ok(
                treatmentService.updateTreatment(id, treatment)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(
            @PathVariable Long id) {

        boolean deleted = treatmentService.deleteTreatment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}