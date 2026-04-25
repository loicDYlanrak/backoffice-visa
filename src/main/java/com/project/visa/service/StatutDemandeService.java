package com.project.visa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return statutDemandeRepository.findTopByDemandeIdOrderByDateChangementStatutDesc(demandeId);
    }
}
