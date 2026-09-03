package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Dentist;
import com.sunrise.dentalclinic.repository.DentistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistService {

    private final DentistRepository dentistRepository;

    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    public Dentist addDentist(Dentist dentist) {
        return dentistRepository.save(dentist);
    }

    public List<Dentist> getAllDentists() {
        return dentistRepository.findAll();
    }

    public Optional<Dentist> getDentistById(Long id) {
        return dentistRepository.findById(id);
    }

    public Optional<Dentist> getDentistByEmail(String email) {
        return dentistRepository.findByEmail(email);
    }

    public Dentist updateDentist(Long id, Dentist updatedDentist) {

        Optional<Dentist> existingDentist = dentistRepository.findById(id);

        if (existingDentist.isPresent()) {

            Dentist dentist = existingDentist.get();

            dentist.setDentistName(updatedDentist.getDentistName());
            dentist.setSpecialization(updatedDentist.getSpecialization());
            dentist.setContactNumber(updatedDentist.getContactNumber());
            dentist.setEmail(updatedDentist.getEmail());

            return dentistRepository.save(dentist);
        }

        return null;
    }

    public boolean deleteDentist(Long id) {

        if (dentistRepository.existsById(id)) {
            dentistRepository.deleteById(id);
            return true;
        }

        return false;
    }
}