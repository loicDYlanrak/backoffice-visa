package com.project.visa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.visa.entity.TypeVisaEntity;
import com.project.visa.repository.TypeVisaRepository;

@Service
public class TypeVisaService {

    private final TypeVisaRepository typeVisaRepository;

    public TypeVisaService(TypeVisaRepository typeVisaRepository) {
        this.typeVisaRepository = typeVisaRepository;
    }
    
    public TypeVisaEntity creerTypeSiInexistant(String libelle) {
        return typeVisaRepository.findByLibelle(libelle)
                .orElseGet(() -> typeVisaRepository.save(new TypeVisaEntity(libelle)));
    }

    public TypeVisaEntity findById(Long id) {
        return typeVisaRepository.findById(id).orElse(null);
    }

    public List<TypeVisaEntity> findAll() {
        return typeVisaRepository.findAll();
    }
}
