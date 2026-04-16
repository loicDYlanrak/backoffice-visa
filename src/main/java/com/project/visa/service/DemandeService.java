package com.project.visa.service;

import com.project.visa.entity.DemandeEntity;
import com.project.visa.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    public List<DemandeEntity> findAll() {
        return demandeRepository.findAll();
    }
    
    public Optional<DemandeEntity> findById(Long id) {
        return demandeRepository.findById(id);
    }
    
    public DemandeEntity save(DemandeEntity demandeEntity) {
        return demandeRepository.save(demandeEntity);
    }
    
    public void deleteById(Long id) {
        demandeRepository.deleteById(id);
    }
    
    public List<DemandeEntity> findByIdStatus(Integer status) {
        return demandeRepository.findByIdStatus(status);
    }
    
    public List<DemandeEntity> searchByName(String name) {
        return demandeRepository.findByReferenceContainingIgnoreCase(name);
    }
    public String genererReference() {
        long count = demandeRepository.count() + 1; // Compter les demandes existantes
        String reference = String.format("RES-%d-%04d", 2026, count); // Générer la référence
        return reference;
    }
    
}