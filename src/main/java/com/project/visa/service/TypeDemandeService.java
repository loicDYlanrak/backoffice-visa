package com.project.visa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.visa.entity.TypeDemandeEntity;
import com.project.visa.repository.TypeDemandeRepository;

@Service
public class TypeDemandeService {

    private final TypeDemandeRepository typeDemandeRepository;

    public TypeDemandeService(TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeRepository = typeDemandeRepository;
    }

    public List<TypeDemandeEntity> findAll() {
        return typeDemandeRepository.findAll();
    }
}
