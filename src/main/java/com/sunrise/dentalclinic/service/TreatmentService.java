package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Treatment;
import com.sunrise.dentalclinic.repository.TreatmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public Treatment addTreatment(Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    public Optional<Treatment> getTreatmentById(Long id) {
        return treatmentRepository.findById(id);
    }

    public Optional<Treatment> getTreatmentByName(String treatmentName) {
        return treatmentRepository.findByTreatmentNameIgnoreCase(treatmentName);
    }

    public Treatment updateTreatment(Long id, Treatment updatedTreatment) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Treatment not found with ID: " + id
                        ));

        treatment.setTreatmentName(updatedTreatment.getTreatmentName());
        treatment.setDescription(updatedTreatment.getDescription());
        treatment.setPrice(updatedTreatment.getPrice());

        return treatmentRepository.save(treatment);
    }

    public boolean deleteTreatment(Long id) {

        if (treatmentRepository.existsById(id)) {
            treatmentRepository.deleteById(id);
            return true;
        }

        return false;
    }
}