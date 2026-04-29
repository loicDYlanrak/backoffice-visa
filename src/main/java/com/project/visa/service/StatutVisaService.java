package com.project.visa.service;

import com.project.visa.entity.StatutVisaEntity;
import com.project.visa.entity.VisaEntity;
import com.project.visa.repository.StatutVisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutVisaService {

    @Autowired
    private StatutVisaRepository statutVisaRepository;

    public StatutVisaEntity save(StatutVisaEntity statutVisa) {
        return statutVisaRepository.save(statutVisa);
    }

    public List<StatutVisaEntity> findAll() {
        return statutVisaRepository.findAll();
    }

    public StatutVisaEntity findById(int id) {
        return statutVisaRepository.findById(id).orElse(null);
    }

    public List<StatutVisaEntity> findByVisa(VisaEntity visa) {
        return statutVisaRepository.findByVisa(visa);
    }

    public Optional<StatutVisaEntity> findLatestByVisa(VisaEntity visa) {
        return statutVisaRepository.findTopByVisaOrderByDateChangementStatutDesc(visa);
    }

    public List<StatutVisaEntity> findByVisaAndStatut(VisaEntity visa, int statut) {
        return statutVisaRepository.findByVisaAndStatut(visa, statut);
    }

    public void delete(StatutVisaEntity statutVisa) {
        statutVisaRepository.delete(statutVisa);
    }

    public void deleteById(int id) {
        statutVisaRepository.deleteById(id);
    }
}