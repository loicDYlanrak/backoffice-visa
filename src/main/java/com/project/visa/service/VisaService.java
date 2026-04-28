package com.project.visa.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.PasseportEntity;
import com.project.visa.entity.VisaEntity;
import com.project.visa.repository.PasseportRepository;
import com.project.visa.repository.TypeVisaRepository;
import com.project.visa.repository.VisaRepository;

import jakarta.transaction.Transactional;

@Service
public class VisaService {

    private final VisaRepository visaRepository;

    public VisaService(VisaRepository visaRepository,
            PasseportRepository passeportRepository,
            TypeVisaRepository typeVisaRepository) {
        this.visaRepository = visaRepository;
    }

    public List<VisaEntity> listerVisasParPasseport(PasseportEntity Passeport) {
        return visaRepository.findByPasseport(Passeport);
    }

    @Transactional
    public void supprimerVisa(Long id) {
        visaRepository.deleteById(id);
    }

    @Transactional
    public VisaEntity save(VisaEntity visa) {
        return visaRepository.save(visa);
    }

    public List<VisaEntity> findByDemandeId(int demandeId) {
        return visaRepository.findByDemandeId(demandeId);
    }

    public VisaEntity createVisaFromDemande(DemandeEntity demande) {
        VisaEntity visa = new VisaEntity();
        visa.setDemande(demande);
        visa.setPasseport(demande.getVisaTransformable().getPasseport());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        visa.setDateDebut(startDate);
        visa.setDateFin(endDate);

        String reference = generateVisaReference(demande);
        visa.setReference(reference);

        return visaRepository.save(visa);
    }

    private String generateVisaReference(DemandeEntity demande) {
        int year = LocalDate.now().getYear();
        return String.format("VIS-%d-%05d", year, demande.getId());
    }
}
