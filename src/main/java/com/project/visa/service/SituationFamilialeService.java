package com.project.visa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.visa.entity.SituationFamilialeEntity;
import com.project.visa.repository.SituationFamilialeRepository;

@Service
public class SituationFamilialeService {

    private final SituationFamilialeRepository situationFamilialeRepository;

    public SituationFamilialeService(SituationFamilialeRepository situationFamilialeRepository) {
        this.situationFamilialeRepository = situationFamilialeRepository;
    }

    public List<SituationFamilialeEntity> findAll() {
        return situationFamilialeRepository.findAll();
    }
}
