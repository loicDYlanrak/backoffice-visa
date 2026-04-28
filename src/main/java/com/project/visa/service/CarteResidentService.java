package com.project.visa.service;

import com.project.visa.entity.*;
import com.project.visa.repository.CarteResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarteResidentService {

    @Autowired
    private CarteResidentRepository carteResidentRepository;

    public CarteResidentEntity save(CarteResidentEntity carteResident) {
        return carteResidentRepository.save(carteResident);
    }

    public CarteResidentEntity findById(Long id) {
        return carteResidentRepository.findById(id).orElse(null);
    }

    public List<CarteResidentEntity> findByDemandeId(int demandeId) {
        return carteResidentRepository.findByDemandeId(demandeId);
    }

    public CarteResidentEntity createCarteResidentFromDemande(DemandeEntity demande) {
        CarteResidentEntity carteResident = new CarteResidentEntity();
        carteResident.setDemande(demande);
        carteResident.setPasseport(demande.getVisaTransformable().getPasseport());
        
        // Calculer les dates de la carte de résident (exemple: 5 ans à partir de la date de traitement)
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(5);
        
        carteResident.setDateDebut(startDate);
        carteResident.setDateFin(endDate);
        
        // Générer une référence unique
        String reference = generateCarteResidentReference(demande);
        carteResident.setReference(reference);
        
        return carteResidentRepository.save(carteResident);
    }

    private String generateCarteResidentReference(DemandeEntity demande) {
        int year = LocalDate.now().getYear();
        return String.format("CR-%d-%05d", year, demande.getId());
    }
}