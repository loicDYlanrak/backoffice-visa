package com.project.visa.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.entity.StatutDemandeEntity;
import com.project.visa.repository.StatutDemandeRepository;

@Service
public class StatutDemandeService {
    @Autowired
    private StatutDemandeRepository statutDemandeRepository;
    public StatutDemandeEntity save(StatutDemandeEntity statutDemandeEntity ){
        return statutDemandeRepository.save(statutDemandeEntity);
    }

    public Optional<StatutDemandeEntity> findLatestByDemandeId(int demandeId) {
        return statutDemandeRepository.findFirstByDemandeIdOrderByIdDesc(demandeId);
    }

    public StatutDemandeEntity addStatut(DemandeEntity demande, Integer statutCode) {
        StatutDemandeEntity statut = new StatutDemandeEntity();
        statut.setDemande(demande);
        statut.setStatut(statutCode);
        statut.setDateChangementStatut(LocalDate.now());
        return statutDemandeRepository.save(statut);
    }

    public boolean hasStatut(DemandeEntity demande, Integer statutCode) {
        return statutDemandeRepository.existsByDemandeAndStatut(demande, statutCode);
    }

}
